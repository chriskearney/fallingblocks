package com.comandante.game.assetmanagement;

import com.comandante.game.board.InvocationRound;
import com.comandante.game.board.logic.GameBlockRenderer;
import com.google.common.math.IntMath;

import java.awt.image.BufferedImage;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import static com.comandante.game.assetmanagement.TileSetGameBlockRenderer.imagesNew;

public class DestructInvoker implements InvocationRound.Invoker<BufferedImage> {

    private final BlockTypeBorder blockTypeBorder;
    private int invokeCount = 0;

    public DestructInvoker(BlockTypeBorder blockTypeBorder) {
        this.blockTypeBorder = blockTypeBorder;
    }

    @Override
    public Optional<BufferedImage> invoke() {
        try {
            if ((invokeCount & 1) == 0) {
                List<BufferedImage> bufferedImages = imagesNew.get(blockTypeBorder);
                return Optional.ofNullable(bufferedImages.get(0));
            } else {
                return Optional.of(GameBlockRenderer.emptyBlackImage);
            }
        } finally {
            invokeCount++;
        }
    }

    @Override
    public int numberRoundsComplete() {
        return IntMath.divide(invokeCount, 2, RoundingMode.CEILING);
    }

    @Override
    public int maxRounds() {
        return 3;
    }

}
