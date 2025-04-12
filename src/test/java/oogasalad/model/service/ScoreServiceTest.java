package oogasalad.model.service;

import static org.junit.jupiter.api.Assertions.*;

import oogasalad.database.DatabaseException;
import oogasalad.database.FirebaseManager;
import oogasalad.model.profile.ScoreData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ScoreServiceTest {

  @BeforeAll
  public static void setUp() throws Exception {
    FirebaseManager.initializeFirebase();
  }

  @Test
  public void saveHighScore_NewHighScore_ReturnTrue() throws DatabaseException {
    String username = "test";
    String game = "test";
    int newScore = 10;

    assertTrue(ScoreService.saveHighScore(username, game, newScore));

    // delete the created result
    boolean result = ScoreService.deleteScore(username, game);
    assertTrue(result);
  }

  @Test
  public void saveHighScore_ExistingHighScore_ReturnFalse() throws DatabaseException {
    String username = "test";
    String game = "test";
    boolean startResult = ScoreService.saveHighScore(username, game, 10);
    assertTrue(startResult);
    boolean sameResult = ScoreService.saveHighScore(username, game, 10);
    assertFalse(sameResult, "score is the same");

    boolean deleteResult = ScoreService.deleteScore(username, game);
    assertTrue(deleteResult);
  }

  @Test
  public void getHighScore_ExistingHighScore_ReturnScoreData() throws DatabaseException {
    String username = "test";
    String game = "test";
    int newScore = 10;
    boolean newScoreResult = ScoreService.saveHighScore(username, game, newScore);
    assertTrue(newScoreResult);

    ScoreData scoreData = ScoreService.getHighScore(username, game);
    assertNotNull(scoreData);
    assertEquals(newScore, scoreData.getScore());
    assertEquals(username, scoreData.getUsername());
    assertEquals(game, scoreData.getGame());

    boolean deleteResult = ScoreService.deleteScore(username, game);
    assertTrue(deleteResult);
  }
}