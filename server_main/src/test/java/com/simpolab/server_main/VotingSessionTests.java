package com.simpolab.server_main;

import static org.junit.jupiter.api.Assertions.*;

import com.simpolab.server_main.voting_session.domain.NoWinnerException;
import com.simpolab.server_main.voting_session.domain.ParticipationStats;
import com.simpolab.server_main.voting_session.domain.Vote;
import com.simpolab.server_main.voting_session.domain.VotingSession;
import com.simpolab.server_main.voting_session.utils.WinnerElection;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.*;
import org.springframework.web.server.ResponseStatusException;

class VotingSessionTests {

  private final SessionDAOMock sessionDAO = new SessionDAOMock();

  private final SessionServiceMock sessionService = new SessionServiceMock();

  @Nested
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  class TestGenericSession {

    VotingSession session = VotingSession
      .builder()
      .id(1L)
      .state(VotingSession.State.ENDED)
      .type(VotingSession.Type.CATEGORIC)
      .endsOn(Date.from(Instant.ofEpochSecond(1668207600000L)))
      .name("Testing Categorical")
      .hasQuorum(false)
      .needAbsoluteMajority(false)
      .build();

    @BeforeEach
    public void setupExampleSession() throws SQLException {
      sessionDAO.create(session);

      sessionDAO.createOption(session.getId(), "Opzione 1");
      sessionDAO.createOption(session.getId(), "Opzione 2");
      sessionDAO.createOption(session.getId(), "Opzione 3");
      sessionDAO.createOption(session.getId(), "Opzione 4");

      List<Vote> votes = new ArrayList<>();
      votes.add(new Vote(1L, 1L));
      votes.add(new Vote(1L, 1L));
      votes.add(new Vote(1L, 1L));
      votes.add(new Vote(1L, 1L));
      votes.add(new Vote(2L, 1L));
      sessionDAO.addVotes(session.getId(), votes);
    }

    @Test
    void sessionExists() {
      val sessionOption = sessionDAO.get(1L);
      assertNotNull(sessionOption.get());
    }

    @Test
    void sessionDoesntExistsShouldThrowResponseStatusException()
      throws NoWinnerException, SQLException {
      val e = assertThrows(
        ResponseStatusException.class,
        () -> {
          WinnerElection.getWinner(null, sessionDAO, 0L);
        }
      );

      assertEquals(e.getMessage(), "404 NOT_FOUND");
    }

    @Test
    void sessionIsNotEndedShouldThrowResponseStatusException()
      throws NoWinnerException, SQLException {
      System.out.println(session.toBuilder().state(VotingSession.State.ACTIVE).build());
      sessionDAO.create(session.toBuilder().state(VotingSession.State.ACTIVE).build());

      val e = assertThrows(
        ResponseStatusException.class,
        () -> {
          val winner = WinnerElection.getWinner(null, sessionDAO, 1L);

          System.out.println(winner);
        }
      );

      assertEquals(e.getMessage(), "403 FORBIDDEN");
    }

    @Test
    void quorumSetAndNotReached() throws NoWinnerException, SQLException {
      sessionDAO.create(session.toBuilder().hasQuorum(true).build());

      sessionDAO.setParticipationsStats(new ParticipationStats(4, 5));

      val e = assertThrows(
        NoWinnerException.class,
        () -> {
          WinnerElection.getWinner(sessionService, sessionDAO, 1L);
        }
      );

      assertEquals(e.getCode(), NoWinnerException.QUORUM_NOT_REACHED);
    }
  }

  @Nested
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  class TestCategoricalAndReferendum {

    VotingSession categorical = VotingSession
      .builder()
      .id(1L)
      .state(VotingSession.State.ENDED)
      .type(VotingSession.Type.CATEGORIC)
      .endsOn(Date.from(Instant.ofEpochSecond(1668207600000L)))
      .name("Testing Categorical and Referendum")
      .hasQuorum(false)
      .needAbsoluteMajority(false)
      .build();

    List<Vote> votes = new ArrayList<>(
      List.of(new Vote(1L, 1L), new Vote(1L, 1L), new Vote(2L, 1L))
    );

    @BeforeAll
    public void setupSessionBeforeAll() throws SQLException {
      sessionDAO.create(categorical);

      sessionDAO.createOption(categorical.getId(), "Opzione 1");
      sessionDAO.createOption(categorical.getId(), "Opzione 2");
      sessionDAO.createOption(categorical.getId(), "Opzione 3");
      sessionDAO.createOption(categorical.getId(), "Opzione 4");

      sessionDAO.setVotes(categorical.getId(), votes);
    }

    @BeforeEach
    public void setupSessionBeforeEach() throws SQLException {
      sessionDAO.create(categorical);
      sessionDAO.setVotes(categorical.getId(), votes);
    }

    @Test
    void facingARunOff() throws NoWinnerException, SQLException {
      sessionDAO.addVotes(categorical.getId(), List.of(new Vote(2L, 1L)));

      val e = assertThrows(
        NoWinnerException.class,
        () -> {
          WinnerElection.getWinner(sessionService, sessionDAO, 1L);
        }
      );

      assertEquals(e.getCode(), NoWinnerException.BALLOTTAGGIO);
    }

    @Test
    void hasAbsMajorityAndIsReached() throws NoWinnerException, SQLException {
      sessionDAO.create(categorical.toBuilder().needAbsoluteMajority(true).build());
      sessionDAO.setParticipationsStats(new ParticipationStats(3, 0));

      val res = WinnerElection.getWinner(sessionService, sessionDAO, 1L);

      assertNotNull(res);
      assertFalse(res.isEmpty());
    }

    @Test
    void hasAbsMajorityAndIsNotReached() throws NoWinnerException, SQLException {
      sessionDAO.create(categorical.toBuilder().needAbsoluteMajority(true).build());
      sessionDAO.addVotes(categorical.getId(), List.of(new Vote(3L, 1L), new Vote(4L, 1L)));
      sessionDAO.setParticipationsStats(new ParticipationStats(5, 4));

      NoWinnerException e = assertThrows(
        NoWinnerException.class,
        () -> {
          WinnerElection.getWinner(sessionService, sessionDAO, 1L);
        }
      );

      assertEquals(e.getCode(), NoWinnerException.NO_ABSOLUTE_MAJORITY);
    }
  }

  @Nested
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  class TestCategoricalWithPreferences {

    VotingSession categoricalWithPrefs = VotingSession
      .builder()
      .id(1L)
      .state(VotingSession.State.ENDED)
      .type(VotingSession.Type.CATEGORIC_WITH_PREFERENCES)
      .endsOn(Date.from(Instant.ofEpochSecond(1668207600000L)))
      .name("Testing Categorical With Preferences")
      .hasQuorum(false)
      .needAbsoluteMajority(false)
      .build();

    List<Vote> votes = new ArrayList<>(
      List.of(new Vote(1L, 1L), new Vote(1L, 1L), new Vote(2L, 1L))
    );

    @BeforeAll
    public void setupSessionBeforeAll() throws SQLException {
      sessionDAO.create(categoricalWithPrefs);

      sessionDAO.createOption(categoricalWithPrefs.getId(), "Opzione 1");
      sessionDAO.createOption(categoricalWithPrefs.getId(), "Opzione 2");
      sessionDAO.createOption(categoricalWithPrefs.getId(), "Opzione 3");
      sessionDAO.createOption(categoricalWithPrefs.getId(), "Opzione 4");

      sessionDAO.setVotes(categoricalWithPrefs.getId(), votes);
    }

    @BeforeEach
    public void setupSessionBeforeEach() throws SQLException {
      sessionDAO.create(categoricalWithPrefs);
      sessionDAO.setVotes(categoricalWithPrefs.getId(), votes);
    }

    @Nested
    class Opzioni {

      @Test
      void facingARunOff() throws NoWinnerException, SQLException {
        sessionDAO.addVotes(categoricalWithPrefs.getId(), List.of(new Vote(2L, 1L)));

        val e = assertThrows(
          NoWinnerException.class,
          () -> {
            WinnerElection.getWinner(sessionService, sessionDAO, 1L);
          }
        );

        assertEquals(e.getCode(), NoWinnerException.BALLOTTAGGIO);
      }

      // si abs e not reached -> exception
      @Test
      void hasAbsMajorityAndIsNotReached() throws NoWinnerException, SQLException {
        sessionDAO.create(categoricalWithPrefs.toBuilder().needAbsoluteMajority(true).build());
        sessionDAO.addVotes(
          categoricalWithPrefs.getId(),
          List.of(new Vote(3L, 1L), new Vote(4L, 1L))
        );
        sessionDAO.setParticipationsStats(new ParticipationStats(5, 4));

        NoWinnerException e = assertThrows(
          NoWinnerException.class,
          () -> {
            WinnerElection.getWinner(sessionService, sessionDAO, 1L);
          }
        );

        assertEquals(e.getCode(), NoWinnerException.NO_ABSOLUTE_MAJORITY);
      }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class Elementi {

      @BeforeAll
      void setupSessioneBeforeAll() throws SQLException {
        sessionDAO.createOption(categoricalWithPrefs.getId(), "Elemento 1 Opzione 1", 1L);
        sessionDAO.createOption(categoricalWithPrefs.getId(), "Elemento 2 Opzione 1", 1L);
        sessionDAO.createOption(categoricalWithPrefs.getId(), "Elemento 1 Opzione 2", 2L);

        List<Vote> lowLevelVotes = new ArrayList<>(votes);
        lowLevelVotes.addAll(List.of(new Vote(5L, 1L), new Vote(5L, 1L), new Vote(6L, 1L)));

        sessionDAO.setVotes(categoricalWithPrefs.getId(), lowLevelVotes);
      }

      @BeforeEach
      void setupSessionBeforeEach() throws SQLException {
        List<Vote> lowLevelVotes = new ArrayList<>(votes);

        lowLevelVotes.addAll(List.of(new Vote(5L, 1L), new Vote(5L, 1L), new Vote(6L, 1L)));

        sessionDAO.setVotes(categoricalWithPrefs.getId(), lowLevelVotes);
      }

      @Test
      void facingARunOff() throws NoWinnerException, SQLException {
        sessionDAO.addVotes(categoricalWithPrefs.getId(), List.of(new Vote(6L, 1L)));

        val e = assertThrows(
          NoWinnerException.class,
          () -> {
            WinnerElection.getWinner(sessionService, sessionDAO, 1L);
          }
        );

        assertEquals(e.getCode(), NoWinnerException.BALLOTTAGGIO_CATEGORICO_PREFERENZE);
      }

      @Test
      void gettingWinner() throws NoWinnerException, SQLException {
        val res = WinnerElection.getWinner(sessionService, sessionDAO, 1L);

        assertNotNull(res);
        assertFalse(res.isEmpty());
        assertArrayEquals(res.toArray(), List.of(1L, 5L).toArray());
      }

      @Test
      void hasAbsMajorityAndIsReached() throws NoWinnerException, SQLException {
        sessionDAO.create(categoricalWithPrefs.toBuilder().needAbsoluteMajority(true).build());
        sessionDAO.setParticipationsStats(new ParticipationStats(3, 0));

        val res = WinnerElection.getWinner(sessionService, sessionDAO, 1L);

        assertNotNull(res);
        assertFalse(res.isEmpty());
      }

      @Test
      void hasAbsMajorityAndIsNotReached() throws NoWinnerException, SQLException {
        sessionDAO.create(categoricalWithPrefs.toBuilder().needAbsoluteMajority(true).build());
        sessionDAO.addVotes(
          categoricalWithPrefs.getId(),
          List.of(new Vote(3L, 1L), new Vote(4L, 1L))
        );
        sessionDAO.setParticipationsStats(new ParticipationStats(5, 4));

        NoWinnerException e = assertThrows(
          NoWinnerException.class,
          () -> {
            WinnerElection.getWinner(sessionService, sessionDAO, 1L);
          }
        );

        assertEquals(e.getCode(), NoWinnerException.NO_ABSOLUTE_MAJORITY);
      }
    }
  }

  @Nested
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  class TestOrdinal {

    VotingSession ordinal = VotingSession
      .builder()
      .id(1L)
      .state(VotingSession.State.ENDED)
      .type(VotingSession.Type.ORDINAL)
      .endsOn(Date.from(Instant.ofEpochSecond(1668207600000L)))
      .name("Testing Ordinal")
      .hasQuorum(false)
      .needAbsoluteMajority(false)
      .build();

    List<Vote> votes = new ArrayList<>(
      List.of(
        new Vote(1L, 1L),
        new Vote(2L, 2L),
        new Vote(3L, 3L),
        new Vote(1L, 1L),
        new Vote(2L, 2L),
        new Vote(3L, 3L),
        new Vote(1L, 1L),
        new Vote(2L, 2L),
        new Vote(3L, 3L),
        new Vote(3L, 1L),
        new Vote(1L, 2L),
        new Vote(2L, 3L)
      )
    );

    @BeforeAll
    public void setupSessionBeforeAll() throws SQLException {
      sessionDAO.create(ordinal);

      sessionDAO.createOption(ordinal.getId(), "Opzione 1");
      sessionDAO.createOption(ordinal.getId(), "Opzione 2");
      sessionDAO.createOption(ordinal.getId(), "Opzione 3");

      sessionDAO.setVotes(ordinal.getId(), votes);
    }

    @BeforeEach
    public void setupSessionBeforeEach() throws SQLException {
      sessionDAO.create(ordinal);
      sessionDAO.setVotes(ordinal.getId(), votes);
    }

    @Test
    void gettingWinner() throws NoWinnerException, SQLException {
      val res = WinnerElection.getWinner(sessionService, sessionDAO, 1L);

      assertNotNull(res);
      assertFalse(res.isEmpty());
      assertArrayEquals(res.toArray(), List.of(1L).toArray());
    }

    @Test
    void hasAbsMajorityAndIsReached() throws NoWinnerException, SQLException {
      sessionDAO.create(ordinal.toBuilder().needAbsoluteMajority(true).build());
      sessionDAO.setParticipationsStats(new ParticipationStats(3, 0));

      val res = WinnerElection.getWinner(sessionService, sessionDAO, 1L);

      assertNotNull(res);
      assertFalse(res.isEmpty());
    }

    @Test
    void hasAbsMajorityAndIsNotReached() throws NoWinnerException, SQLException {
      sessionDAO.create(ordinal.toBuilder().needAbsoluteMajority(true).build());
      sessionDAO.addVotes(
        ordinal.getId(),
        List.of(
          new Vote(2L, 1L),
          new Vote(1L, 2L),
          new Vote(3L, 3L),
          new Vote(2L, 1L),
          new Vote(1L, 2L),
          new Vote(3L, 3L)
        )
      );
      sessionDAO.setParticipationsStats(new ParticipationStats(6, 0));

      NoWinnerException e = assertThrows(
        NoWinnerException.class,
        () -> {
          WinnerElection.getWinner(sessionService, sessionDAO, 1L);
        }
      );

      assertEquals(e.getCode(), NoWinnerException.NO_ABSOLUTE_MAJORITY);
    }
  }
}
