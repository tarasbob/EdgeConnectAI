public class PlayGame {

    public static void main(String[] args){
        //playSelf();
        playHuman();
    }

    public static void playSelf(){
        GameState state = new NimState(15);
        GameMove move;
        while(!state.getLegalMoves().isEmpty()){
            if(state.getPlayerJustMoved() == 1){
                move = UCT.findMove(state, 10000);
            } else {
                move = UCT.findMove(state, 10);
            }
            System.out.println(move);
            state.doMove(move);
        }
        if(state.getResult(state.getPlayerJustMoved()) == 1.0){
            System.out.println("Player " + Integer.toString(state.getPlayerJustMoved()) + " wins!");
        } else if(state.getResult(state.getPlayerJustMoved()) == 0.0){
            System.out.println("Player " + Integer.toString(3 - state.getPlayerJustMoved()) + " wins!");
        } else {
            System.out.println("Nobody wins");
        }
    }

    public static void playHuman(){
        GameState state = new NimState(100);
        GameMove move;
        while(!state.getLegalMoves().isEmpty()){
            System.out.println(state);
            if(state.getPlayerJustMoved() == 1){
                move = UCT.findMove(state, 100000);
                System.out.println(move);
            } else {
                //get human input
                String input = System.console().readLine();
                move = new NimMove(Integer.parseInt(input));
            }
            state.doMove(move);
        }
        if(state.getResult(state.getPlayerJustMoved()) == 1.0){
            System.out.println("Player " + Integer.toString(state.getPlayerJustMoved()) + " wins!");
        } else if(state.getResult(state.getPlayerJustMoved()) == 0.0){
            System.out.println("Player " + Integer.toString(3 - state.getPlayerJustMoved()) + " wins!");
        } else {
            System.out.println("Nobody wins");
        }
    }
}

