package com.vw.digitalhub.robotcleaner.domain

/**
 * Robot domain entity. Executes commands with boundary limits.
 */
data class Robot(var position: Position, var direction: Direction) {
    fun execute(command: Char, bounds: Position) {
        when(command){
            'L'->direction=direction.left()
            'R'->direction=direction.right()
            'M'->{
                val next=direction.moveForward(position)
                if(next.x in 0..bounds.x && next.y in 0..bounds.y) position=next
            }
        }
    }
    override fun toString(): String = "${position.x}${position.y} ${direction}"

}
