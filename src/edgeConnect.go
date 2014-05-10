package main

import (
    "fmt"
    "math"
    "math/rand"
)


//////////////////////////// GameState

type Coordinate struct {
    x int
    y int
    z int
}

type GameState struct {
    playerJustMoved int
    movesLeft int
    size int
    specialCells [5]Coordinate
    board map[Coordinate]int
}


func (s *GameState) New(sz int){
    s.size = sz
    s.playerJustMoved = 2
    s.movesLeft = 0
    s.board = make(map[Coordinate]int)
}


func (s *GameState) Clone() *GameState {
    g = new(GameState)
    g.size = sz
    g.playerJustMoved = s.playerJustMoved
    g.movesLeft = s.movesLeft
    
    for k, v := range s.board {
        g.board[k] = v
    }
}

func (s *GameState) DoMove(move Coordinate) {
    if s.board[move] != 0 {
        panic("Cell is not empty!")
    } else if len(s.board) >= 3 * size * (size + 1) {
        panic("No Empty Cells left")
    } else {
        if s.movesLeft == 0 {
            s.playerJustMoved = 3 - s.playerJustMoved
            s.movesLeft = 1
        } else {
            s.movesLeft = 0
        }
        s.board[move] = s.playerJustMoved
    }
}

func (s *GameState) Rollout(){
    //find a random permutation of cells, and fill them in one by one
}

func (s *GameState) GetMoves() []Coordinate {

}

func (s *GameState) GetResult(jm int) int {
    return 0
}

//////////////// End of Game State


///////////////////// Node

type Node struct {
    move Coordinate
    parentNode *Node
    childNodes []*Node
    wins int
    visits int
    untriedMoves []Coordinate
    playerJustMoved int
}

func (n *Node) New(move []Coordinate, parent *Node, state *GameState){
    n.move = move
    n.parentNode = parent
    n.untriedMoves = state.GetMoves()
    n.playerJustMoved = state.playerJustMoved
}

func getRating(cwins_i, cvisits_i, pvisits_i int) float64{
    cwins := float64(cwins_i)
    cvisits := float64(cvisits_i)
    pvisits := float64(pvisits_i)
    K := 1.0

    return cwins/cvisits + K * math.Sqrt(2 * math.Log(pvisits) / cvisits)
}

func (n *Node) UCTSelectChild() *Node {
    maxVal := -1.0
    var maxNode *Node

    for _, child := range n.childNodes {
        curVal := getRating(child.wins, child.visits, n.visits)
        if curVal > maxVal {
            maxVal = curVal
            maxNode = child
        }
    }

    return maxNode
}

func (n *Node) Expand(state *GameState) *Node {
    //Pick a random move and remove it from untried moves
    i := randInt(len(n.untriedMoves))
    move := n.untriedMoves[i]
    n.untriedMoves[i] = n.untriedMoves[0]
    n.untriedMoves = n.untriedMoves[1:]

    //Execute the move
    state.DoMove(move)

    //Create a child node associated with the move and return it
    child := new(Node)
    child.New(move, n, state)
    n.childNodes = append(n.childNodes, child)
    return child
}

func (n *Node) Update(result int) {
    n.visits += 1
    n.wins += result
}

////////////////////// end of Node

func UCT(rootState *NimState, itermax int) int{
    rootNode := new(Node)
    rootNode.New(-1, nil, rootState)

    for i := 0; i < itermax; i++ {
        node := rootNode
        state := rootState.Clone()

        //select
        for len(node.untriedMoves) == 0 && len(node.childNodes) != 0 {
            node = node.UCTSelectChild()
            state.DoMove(node.move)
        }

        //expand
        if len(node.untriedMoves) != 0 {
            state = node.Expand()
        }

        //rollout
        state.Rollout()

        //backpropagate
        for node != nil {
            node.Update(state.GetResult(node.playerJustMoved))
            node = node.parentNode
        }
    }

    //find most visited node
    maxVisits := -1
    var bestMove int
    for _, node := range rootNode.childNodes {
        if node.visits > maxVisits {
            maxVisits = node.visits
            bestMove = node.move
        }
    }
    return bestMove
}

func main() {
    state := new(NimState)
    state.New(100)
    move := UCT(state, 1000000)
    fmt.Println(move)
}
