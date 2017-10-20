package com.teamtreehouse.techdegree.overboard.model;

import com.teamtreehouse.techdegree.overboard.exc.AnswerAcceptanceException;
import com.teamtreehouse.techdegree.overboard.exc.VotingException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.is;

import static org.junit.Assert.*;


public class UserTest {
    private Board board;
    private User questioner;
    private User answerer;
    private User anonymous;
    private Question question;
    private Answer answer;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        this.board = new Board("Board Topic");
        this.questioner = new User(board, "Amber");
        this.question = new Question(questioner, "Should I stay or should I go now?");
        this.answerer = new User(board, "MaryAnne");
        this.answer = new Answer(question, answerer, "If you go there will be trouble, and if you stay it will be double");
        this.anonymous = new User(board, "Bill Board");
    }

    @Test
    public void questionersReputationGoesUpBy5WhenUpVoted() throws Exception {
        //Arrange
        board.addQuestion(question);
        //Act
        answerer.upVote(question);
        //Assert
        assertEquals(5, questioner.getReputation());
    }
    @Test
    public void answererReputationGoesUpBy10WhenUpVoted() throws Exception {
        //Arrange
        board.addQuestion(question);
        board.addAnswer(answer);
        //Act
        questioner.upVote(answer);
        //Assert
        assertEquals(10, answerer.getReputation());
    }
    @Test
    public void havingAnAnswerAcceptedIncreaseReputationBy15() throws Exception {
        //Arrange
        board.addQuestion(question);
        board.addAnswer(answer);
        //Act
        questioner.acceptAnswer(answer);
        //Assert
        assertEquals(15, answerer.getReputation());
    }
    @Test(expected = VotingException.class)
    public void selfVotingIsNotAllowedinQuestions() throws Exception {
        //Arrange
        board.addQuestion(question);
        //Act
        questioner.upVote(question);
        //Assert
    }
    @Test(expected = VotingException.class)
    public void selfVotingIsNotAllowedinAnswers() throws Exception {
        //Arrange
        board.addQuestion(question);
        board.addAnswer(answer);
        //Act
        answerer.upVote(answer);
        //Assert
    }
    @Test
    public void onlyOriginalQuestionerCanAcceptAnswer() throws Exception {
        thrown.expect(AnswerAcceptanceException.class);
        String excMessage = "Only Amber can accept this answer as it is their question";
        thrown.expectMessage(excMessage);
        //Arrange
        board.addQuestion(question);
        board.addAnswer(answer);
        //Act
        anonymous.acceptAnswer(answer);
        //Assert
    }
    @Test
    public void questionerReputationRemainsTheSameWhenDownVoted() throws Exception {
        //Arrange
        board.addQuestion(question);
        //Act
        anonymous.downVote(question);
        //Assert
        assertEquals(0, questioner.getReputation());
    }
    @Test
    public void answererReputationDecreaseBy1WhenDownVoting() throws Exception {
        //Arrange
        board.addQuestion(question);
        board.addAnswer(answer);
        //Act
        anonymous.downVote(answer);
        //Assert
        assertEquals(-1, answerer.getReputation());
    }
}