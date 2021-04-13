package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.gui.Info;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * A class that represents a process of a game.
 *
 * @author Aidas Venckunas (325464)
 * @author Shin Urech (327245)
 */
public final class Game {

    private Game(){}

    /**
     * A method that is playing the game.
     *
     * @param players a map of playerID mapped to Players
     * @param playerNames a map of playerID mapped to their names
     * @param tickets tickets that are used for a game
     * @param rng random generator
     * @throws IllegalArgumentException if the number of players is not equal to 2 or the number of player names is
     * not equal to 2
     */
    public static void play(Map<PlayerId, Player> players, Map<PlayerId, String> playerNames,
                            SortedBag<Ticket> tickets, Random rng){

        Preconditions.checkArgument(players.size() == 2 && playerNames.size() == 2);

        GameState gameState = GameState.initial(tickets, rng);

        //info for player1 and player2
        Info info_Player1 = new Info(playerNames.get(PlayerId.PLAYER_1));
        Info info_Player2 = new Info(playerNames.get(PlayerId.PLAYER_2));

        ///Before the start of the game

        //communicating each player their identities and names
        for(Map.Entry<PlayerId, Player> entry : players.entrySet()) {
            entry.getValue().initPlayers(entry.getKey(), playerNames);
        }

        //sending info about who will play first
        if(gameState.currentPlayerId().equals(PlayerId.PLAYER_1)){
            sendInfo(info_Player1.willPlayFirst(), players);
        }
        else sendInfo(info_Player2.willPlayFirst(), players);

        //distributing the tickets players initially receive
        for(Map.Entry<PlayerId, Player> entry : players.entrySet()) {

            if(entry.getKey().equals(PlayerId.PLAYER_1)){
                entry.getValue().setInitialTicketChoice(gameState.topTickets(Constants.INITIAL_TICKETS_COUNT));
            }
            else{
                entry.getValue().setInitialTicketChoice(gameState.withoutTopTickets(Constants.INITIAL_TICKETS_COUNT)
                        .topTickets(Constants.INITIAL_TICKETS_COUNT));
            }
        }

        sendUpdate(gameState, players);

        //communicating the tickets players initially choose
        SortedBag <Ticket> choice_player1 = players.get(PlayerId.PLAYER_1).chooseInitialTickets();
        gameState = gameState.withInitiallyChosenTickets(PlayerId.PLAYER_1, choice_player1);
        SortedBag <Ticket> choice_player2 = players.get(PlayerId.PLAYER_2).chooseInitialTickets();
        gameState = gameState.withInitiallyChosenTickets(PlayerId.PLAYER_2, choice_player2);

        //sending the info about the number of tickets they choose
        sendInfo(info_Player1.keptTickets(choice_player1.size()), players);
        sendInfo(info_Player2.keptTickets(choice_player2.size()), players);

        gameState = gameState.withoutTopTickets(2 * Constants.INITIAL_TICKETS_COUNT);

        sendUpdate(gameState, players);

        ///The game begins

        do{
            Player currentPlayer = players.get(gameState.currentPlayerId());
            Info current_info;

            //defining an info of a current player and sending it
            if(gameState.currentPlayerId().equals(PlayerId.PLAYER_1)){
                current_info = info_Player1;
            }
            else current_info = info_Player2;

            sendInfo(current_info.canPlay(), players);

            sendUpdate(gameState, players);

            Player.TurnKind nextTurn = currentPlayer.nextTurn();

            //A kind of player's turn
            switch (nextTurn){

                case DRAW_TICKETS:

                    sendInfo(current_info.drewTickets(Constants.IN_GAME_TICKETS_COUNT), players);

                    SortedBag <Ticket> choice =  currentPlayer.
                            chooseTickets(gameState.topTickets(Constants.IN_GAME_TICKETS_COUNT));

                    gameState = gameState.withChosenAdditionalTickets(gameState.
                                    topTickets(Constants.IN_GAME_TICKETS_COUNT), choice);

                    sendInfo(current_info.keptTickets(choice.size()), players);

                    break;

                case DRAW_CARDS:

                    //first choice
                    gameState = gameState.withCardsDeckRecreatedIfNeeded(rng);
                    int choice1 = currentPlayer.drawSlot();

                    if(choice1 == Constants.DECK_SLOT){

                        gameState = gameState.withBlindlyDrawnCard();
                        sendInfo(current_info.drewBlindCard(), players);
                    }
                    else{
                        sendInfo(current_info.drewVisibleCard(gameState.cardState().faceUpCard(choice1)), players);
                        gameState = gameState.withDrawnFaceUpCard(choice1);

                    }

                    sendUpdate(gameState, players);

                    //second choice
                    gameState = gameState.withCardsDeckRecreatedIfNeeded(rng);
                    int choice2 = currentPlayer.drawSlot();

                    if(choice2 == Constants.DECK_SLOT){

                        gameState = gameState.withBlindlyDrawnCard();
                        sendInfo(current_info.drewBlindCard(), players);
                    }
                    else{
                        sendInfo(current_info.drewVisibleCard(gameState.cardState().faceUpCard(choice2)), players);
                        gameState = gameState.withDrawnFaceUpCard(choice2);
                    }

                    break;

                case CLAIM_ROUTE:

                    //firstly player chooses a route and lays initialClaimCards
                    Route route = currentPlayer.claimedRoute();
                    SortedBag <Card> cards = currentPlayer.initialClaimCards();

                    //if it is a tunnel..
                    if(route.level().equals(Route.Level.UNDERGROUND)){

                        sendInfo(current_info.attemptsTunnelClaim(route, cards), players);

                        //additional cards are drawn and additional card count is calculated
                        SortedBag.Builder <Card> builder = new SortedBag.Builder<>();

                        for(int i = 0; i < Constants.ADDITIONAL_TUNNEL_CARDS; ++i){
                            gameState = gameState.withCardsDeckRecreatedIfNeeded(rng);
                            builder.add(gameState.topCard());
                            gameState = gameState.withoutTopCard();

                        }
                        SortedBag <Card> additionalDrawnCards = builder.build();

                        int additionalCardsCount = route.additionalClaimCardsCount(cards, additionalDrawnCards);

                        sendInfo(current_info.drewAdditionalCards(additionalDrawnCards, additionalCardsCount), players);

                        //if additional cards count is positive and player has additional cards needed..
                        if(additionalCardsCount > 0 && hasAdditionalCardsNeeded(additionalCardsCount, cards,
                                additionalDrawnCards, gameState)){

                            //Player chooses which cards to use as additional cards
                            SortedBag <Card> chosenAdditionalCards = currentPlayer.chooseAdditionalCards(gameState
                                    .currentPlayerState()
                                    .possibleAdditionalCards(additionalCardsCount,cards, additionalDrawnCards));

                            //if a player chooses any cards, we add a route to a player
                            if(chosenAdditionalCards.size() != 0){
                                sendInfo(current_info.claimedRoute(route, cards.union(chosenAdditionalCards)), players);
                                gameState = gameState.withClaimedRoute(route, cards.union(chosenAdditionalCards));
                            }
                            else{
                                sendInfo(current_info.didNotClaimRoute(route), players);
                            }
                        }
                        else if(additionalCardsCount == 0){

                            sendInfo(current_info.claimedRoute(route, cards), players);
                            gameState = gameState.withClaimedRoute(route, cards);
                        }
                        else{
                            sendInfo(current_info.didNotClaimRoute(route), players);
                        }
                        gameState = gameState.withMoreDiscardedCards(additionalDrawnCards);
                    }
                    //if it is an overground route..
                    else{
                        sendInfo(current_info.claimedRoute(route, cards), players);
                        gameState = gameState.withClaimedRoute(route, cards);
                    }

                    break;
            }

            if(gameState.currentPlayerId().equals(gameState.lastPlayer())){

                sendUpdate(gameState, players);

                int[] points = final_points(PlayerId.PLAYER_1, PlayerId.PLAYER_2, gameState, players,
                        info_Player1, info_Player2);

                if(points[0] > points[1]){
                    sendInfo(info_Player1.won(points[0], points[1]), players);
                }
                else if (points[0] == points[1]){
                    sendInfo(info_Player1.draw(new ArrayList<String>(playerNames.values()), points[0]), players);
                }
                else{
                    sendInfo(info_Player2.won(points[1], points[0]), players);
                }

                break;
            }

            ///End of the game
            if(gameState.lastTurnBegins()){
                sendInfo(current_info.lastTurnBegins(gameState.currentPlayerState().carCount()), players);
            }

            gameState = gameState.forNextTurn();

        }while(true);
    }

    private static void sendInfo(String info, Map<PlayerId, Player> players){

        for(Map.Entry<PlayerId, Player> entry : players.entrySet()) {

            entry.getValue().receiveInfo(info);
        }
    }

    private static void sendUpdate(GameState current, Map<PlayerId, Player> players){

        for(Map.Entry<PlayerId, Player> entry : players.entrySet()) {

            entry.getValue().updateState(current, current.playerState(entry.getKey()));
        }
    }

    private static boolean hasAdditionalCardsNeeded(int additionalCardsCount, SortedBag <Card> cards,
                SortedBag <Card> additionalDrawnCards, GameState gameState){



        List<SortedBag<Card>> all_possibilities = gameState.currentPlayerState().
                        possibleAdditionalCards(additionalCardsCount,cards, additionalDrawnCards);

        SortedBag<Card> available_cards = gameState.currentPlayerState().cards().difference(cards);

        if(additionalCardsCount > available_cards.size()){
            return false;
        }

        List <SortedBag<Card>> player_possibilities = new ArrayList<>(available_cards.subsetsOfSize(additionalCardsCount));

        for(SortedBag <Card> possibility : all_possibilities){
            for(SortedBag <Card> player_possibility : player_possibilities){
                if (possibility.equals(player_possibility)){
                    return true;
                }
            }
        }
        return false;
    }

    private static int[] final_points(PlayerId player1, PlayerId player2, GameState gameState,
                                      Map<PlayerId, Player> players, Info info1, Info info2){

        int player1_points = gameState.playerState(player1).finalPoints();
        int player2_points = gameState.playerState(player2).finalPoints();
        Trail player1_longest = Trail.longest(gameState.playerState(player1).routes());
        Trail player2_longest = Trail.longest(gameState.playerState(player2).routes());

        if(player1_longest.length() > player2_longest.length()){

            player1_points += Constants.LONGEST_TRAIL_BONUS_POINTS;
            sendInfo(info1.getsLongestTrailBonus(player1_longest), players);
        }
        else if(player1_longest.length() == player2_longest.length()){

            player1_points += Constants.LONGEST_TRAIL_BONUS_POINTS;
            player2_points += Constants.LONGEST_TRAIL_BONUS_POINTS;
            sendInfo(info1.getsLongestTrailBonus(player1_longest), players);
            sendInfo(info2.getsLongestTrailBonus(player2_longest), players);
        }
        else{
            player2_points += Constants.LONGEST_TRAIL_BONUS_POINTS;
            sendInfo(info2.getsLongestTrailBonus(player2_longest), players);
        }

        int[] points = new int[2];
        points[0] = player1_points;
        points[1] = player2_points;

        return points;
    }
}
