package com.vw.digitalhub.robotcleaner.domain

/** Enum for four directions. Provides rotation and movement behavior. */
enum class Direction {
    N, E, S, W;
    fun left() = when(this){ N->W; W->S; S->E; E->N }
    fun right() = when(this){ N->E; E->S; S->W; W->N }
    fun moveForward(p: Position)=when(this){
        N->p.copy(y=p.y+1); S->p.copy(y=p.y-1); E->p.copy(x=p.x+1); W->p.copy(x=p.x-1)
    }
}
