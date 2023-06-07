package ir.ac.ut.ie.Entities;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ir.ac.ut.ie.Exceptions.InvalidCommand;
import ir.ac.ut.ie.Repository.VoteRepository;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "Comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer commentId;
    private String userEmail;
    private Integer commodityId;
    private String text;
    private String username;
    private Date date;
    private int commentLike;
    private int commentDislike;

    public Comment() {
    }


    public Comment(String userEmail, Integer commodityId, String text, String username) {
        this.userEmail = userEmail;
        this.text = text;
        this.username = username;
        commentLike = 0;
        commentDislike = 0;
        this.commodityId = commodityId;
//        date = new Date();
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public void initialValues() {
        commentLike = 0;
        commentDislike = 0;
    }

    public void addVote(Vote vote, VoteRepository voteRepository) {
        Vote voteOb = voteRepository.findVoteByUsernameAndCommentId(vote.getUsername(), vote.getCommentId());

        if (voteOb != null) {
            if (voteOb.getVote() == 1)
                commentLike -= 1;
            if (voteOb.getVote() == -1)
                commentDislike -= 1;
            voteRepository.deleteById(voteOb.getVoteID());
        }
        updateLikeDislike(vote);

    }

    private void updateLikeDislike(Vote vote) {
        if (vote.getVote() == 1)
            commentLike += 1;
        if (vote.getVote() == -1)
            commentDislike += 1;
    }

    public ObjectNode getInformation(ObjectMapper mapper) {
        ObjectNode commentMapper = mapper.createObjectNode();
        commentMapper.put("userEmail", userEmail);
        commentMapper.put("text", text);
        commentMapper.put("like", commentLike);
        commentMapper.put("dislike", commentDislike);
        return commentMapper;
    }

    public void checkForInvalidCommand() throws InvalidCommand {
        if (userEmail == null || commodityId == null || text == null || date == null)
            throw new InvalidCommand();
    }

    public Integer getCommentId() {
        return commentId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Integer getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(Integer commodityId) {
        this.commodityId = commodityId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public int getCommentLike() {
        return commentLike;
    }

    public int getCommentDislike() {
        return commentDislike;
    }

}
