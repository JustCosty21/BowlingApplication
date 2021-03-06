package com.dougestep.bowling.impl;

import com.dougestep.bowling.GameManager;
import com.dougestep.bowling.data.Bowler;
import com.dougestep.bowling.data.BowlingFrame;
import com.dougestep.bowling.data.Game;
import com.google.common.base.Verify;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manages a bowling game.
 *
 * @author dougestep
 */
public final class GameManagerImpl implements GameManager {
    /**
     * 10 points.
     */
    private static final int MARK_1 = 10;
    /**
     * 20 points.
     */
    private static final int MARK_2 = 20;
    /**
     * 30 points.
     */
    private static final int MARK_3 = 30;
    /**
     * Tenth frame.
     */
    private static final int LAST_FRAME = 10;
    /**
     * The score for a perfect game.
     */
    private static final int PERFECT_GAME = 300;
    private final Game game;
    private final Map<Integer, BowlingFrame> frames;

    /**
     * Creates an instance of this class.
     *
     * @param player the bowler.
     */
    private GameManagerImpl(final Bowler player) {
        frames = new HashMap<>();
        game = new Game().setUid(UUID.randomUUID());
        game.setBowler(player);
    }

    /**
     * Creates an instance of this class.
     *
     * @param player the bowler.
     * @return the instance.
     */
    public static GameManager newGame(final Bowler player) {
        return new GameManagerImpl(player);
    }

    @Override
    public GameManager addFrames(final BowlingFrame[] frames) {
        if (frames == null || frames.length == 0) {
            return this;
        }

        for (final BowlingFrame frame : frames) {
            addFrame(frame);
        }

        return this;
    }

    @Override
    public GameManager addFrame(final BowlingFrame frame) {
        final int frameNumber = frames.size() + 1;
        frames.put(frameNumber, frame);

        return calculateScore();
    }

    /**
     * Asserts the properties of the supplied {@link BowlingFrame} are valid.
     *
     * @param frame the frame.
     */
    private void assertValidFrame(final BowlingFrame frame) {
        Verify.verifyNotNull(frame, "expected a non-null reference to %s", "FrameBean");
        Verify.verify(frame.getFirstBall() >= 0 && frame.getFirstBall() <= MARK_1,
                "Invalid value for the first ball: %s", frame.getFirstBall());
        Verify.verify(frame.getSecondBall() >= 0 && frame.getSecondBall() <= MARK_1,
                "Invalid value for the second ball: %s", frame.getSecondBall());
        Verify.verify(frame.getScore() >= 0 && frame.getScore() <= PERFECT_GAME,
                "The score must be a value between 0 and 300. Received: %s", frame.getScore());
        Verify.verify(frame.getFrameTotal() <= MARK_1,
                "The sum of the first and second ball can not be greater than 10.");
    }

    @Override
    public GameManager calculateScore() {
        final int topFrame = frames.size();
        for (int frameNumber = 1; frameNumber <= topFrame; frameNumber++) {
            final BowlingFrame currentFrame = frames.get(frameNumber);
            if (currentFrame.isOpenFrame()) {
                scoreOpenFrame(frameNumber, currentFrame);
            } else if (currentFrame.isSpare()) {
                scoreSpareFrame(frameNumber, currentFrame);
            } else if (currentFrame.isStrike()) {
                scoreStrikeFrame(frameNumber, currentFrame);
            }
        }

        setFramesToGame();
        setFinishedGameProperties();

        return this;
    }

    /**
     * Sets the score on a frame where the bowler didn't get a strike or a spare.
     *
     * @param frameNumber the frame number for the open frame.
     * @param frame       the bowling frame for the open frame.
     */
    private void scoreOpenFrame(final int frameNumber, final BowlingFrame frame) {
        final int priorFrameScore = getFrameScore(frameNumber, -1);
        frame.setScore(priorFrameScore + frame.getFrameTotal());
    }

    /**
     * Sets the score on a frame where the bowler bowled a spare.
     *
     * @param frameNumber the frame number for the spare frame.
     * @param frame       the bowling frame for the spare frame.
     */
    private void scoreSpareFrame(final int frameNumber, final BowlingFrame frame) {
        final int priorFrameScore = getFrameScore(frameNumber, -1);
        final BowlingFrame nextFrame = getFrame(frameNumber, 1);

        // the original spare(10)
        int score = priorFrameScore + MARK_1;
        if (nextFrame != null) {
            // the original spare(10) + the first ball.
            score += nextFrame.getFirstBall();
        }

        frame.setScore(score);
    }

    /**
     * Sets the score on a frame where the bowler bowled a strike.
     *
     * @param frameNumber the frame number for the strike frame.
     * @param frame       the bowling frame for the strike frame.
     */
    private void scoreStrikeFrame(final int frameNumber, final BowlingFrame frame) {
        final int priorFrameScore = getFrameScore(frameNumber, -1);
        final BowlingFrame nextFrame = getFrame(frameNumber, 1);
        if (nextFrame == null) {
            // no next ball.
            frame.setScore(priorFrameScore + MARK_1);
        } else {
            if (nextFrame.isOpenFrame()) {
                // the original strike(10) + the next two balls.
                frame.setScore(priorFrameScore + MARK_1 + nextFrame.getFrameTotal());
            } else if (nextFrame.isSpare()) {
                // the original strike(10) + the spare(10)
                frame.setScore(priorFrameScore + MARK_2);
            } else if (nextFrame.isStrike()) {
                // get the next ball after nextFrame (two balls from the original ball)
                final BowlingFrame twoFramesAhead = getFrame(frameNumber, 2);
                if (twoFramesAhead == null) {
                    // no next ball after nextFrame... the original strike(10) + strike(10)
                    frame.setScore(priorFrameScore + MARK_2);
                } else {
                    if (twoFramesAhead.isStrike()) {
                        // original strike(10) + the second strike(10) + the third strike(10)
                        frame.setScore(priorFrameScore + MARK_3);
                    } else {
                        // original strike(10) + the second strike(10) + the first ball.
                        frame.setScore(priorFrameScore + MARK_2 + twoFramesAhead.getFirstBall());
                    }
                }
            }
        }
    }

    /**
     * Replaces all frames in the supplied game with the latest set.
     */
    private void setFramesToGame() {
        game.clearFrames();

        final int topFrame = frames.size();
        for (int frameNumber = 1; frameNumber <= topFrame; frameNumber++) {
            final BowlingFrame frame = frames.get(frameNumber);
            game.addFrame(frame);
        }
    }

    /**
     * Sets the properties of a finished game.
     */
    private void setFinishedGameProperties() {
        final int topFrame = frames.size();
        final BowlingFrame lastFrame;
        if (topFrame < LAST_FRAME) {
            lastFrame = frames.get(topFrame);
        } else {
            lastFrame = frames.get(LAST_FRAME);
        }
        game.setScore(lastFrame.getScore());

        setGameCompleteFlag(lastFrame);
    }

    /**
     * Sets the complete flag for the game.
     *
     * @param lastFrame the last frame that the bowler has bowled in that could contain a score.
     */
    private void setGameCompleteFlag(final BowlingFrame lastFrame) {
        game.setComplete(false); // assume not complete

        final int topFrame = frames.size();
        if (topFrame >= LAST_FRAME) {
            // in the tenth frame
            if (lastFrame.isOpenFrame()) {
                // complete if the first frame in the tenth is an open
                game.setComplete(true);
            } else if (lastFrame.isSpare()) {
                final BowlingFrame nextFrame = getFrame(LAST_FRAME, 1);
                // complete if the first frame in the 10th is a spare and the next ball has been thrown.
                game.setComplete(nextFrame != null && nextFrame.getFirstBall() >= 0);
            } else if (lastFrame.isStrike()) {
                final BowlingFrame nextFrame = getFrame(LAST_FRAME, 1);
                if (nextFrame != null && !nextFrame.isStrike()) {
                    // complete if the first frame in the 10th is a strike and the next frame is a spare or an
                    // open.
                    game.setComplete(nextFrame.getFirstBall() >= 0 && nextFrame.getSecondBall() >= 0);
                } else {
                    // complete if the first two frames in the tenth are strikes and the first ball of the
                    // last frame has been thrown.
                    final BowlingFrame next2Frame = getFrame(LAST_FRAME, 2);
                    game.setComplete(next2Frame != null && next2Frame.getFirstBall() >= 0);
                }
            }
        }
    }

    /**
     * Returns the score for the {@link BowlingFrame} at the supplied frameNumber + the supplied framesAhead
     * position.
     *
     * @param frameNumber the frame number to start at. The returned frame will be the frame associated with
     *                    this frame number + the framesAhead value.
     * @param framesAhead the number of frames to added to the supplied frame number when retrieving the
     *                    frame.
     * @return the score or zero if the frame doesn't exist.
     */
    private int getFrameScore(final int frameNumber, final int framesAhead) {
        final BowlingFrame frameBean = getFrame(frameNumber, framesAhead);
        return frameBean == null ? 0 : frameBean.getScore();
    }

    @Override
    public GameManager replaceFrame(final int frameNumber, final BowlingFrame frame) {
        final BowlingFrame retrFrame = retrieveFrame(frameNumber);
        Verify.verify(retrFrame != null, "Frame not found for frame number %s", frameNumber);

        assertValidFrame(frame);

        frames.put(frameNumber, frame);

        return calculateScore();
    }

    @Override
    public GameManager deleteFrame(final int frameNumber) {
        final BowlingFrame frame = retrieveFrame(frameNumber);
        if (frame == null) {
            return this;
        }

        frames.remove(frameNumber);

        return calculateScore();
    }

    @Override
    public BowlingFrame retrieveFrame(final int frameNumber) {
        return getFrame(frameNumber, 0);
    }

    /**
     * Returns the {@link BowlingFrame} at the supplied frameNumber + the supplied framesAhead position.
     *
     * @param frameNumber the frame number to start at. The returned frame will be the frame associated with
     *                    this frame number + the framesAhead value.
     * @param framesAhead the number of frames to add to the supplied frame number when retrieving the frame.
     * @return the {@link BowlingFrame} or null if the frame doesn't exist.
     */
    private BowlingFrame getFrame(final int frameNumber, final int framesAhead) {
        final int topFrame = frames.size();
        BowlingFrame frameBean = null;
        final int frame = frameNumber + framesAhead;
        if (frame >= 1 && frame <= topFrame) {
            frameBean = frames.get(frame);
        }
        return frameBean;
    }

    @Override
    public Game getGame() {
        return game;
    }
}
