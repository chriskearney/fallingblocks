package com.comadante;

public class StandardMagicBlockProcessor implements MagicBlockProcessor
{
    @Override
    public boolean process(GameBoard gameBoard) {
        System.out.println("The processor.");
        return false;
    }
}
