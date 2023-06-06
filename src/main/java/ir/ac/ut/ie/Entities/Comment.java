package ir.ac.ut.ie.Entities;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ir.ac.ut.ie.Exceptions.InvalidCommand;
import ir.ac.ut.ie.Repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;

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
    private int like;
    private int dislike;

    public Comment() {
    }


    public Comment(String userEmail, Integer commodityId, String text, String username) {
        this.userEmail = userEmail;
        this.text = text;
        this.username = username;
        like = 0;
        dislike = 0;
        this.commodityId = commodityId;
//        date = new Date();
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public void initialValues() {
        like = 0;
        dislike = 0;
    }

    public void addVote(Vote vote, VoteRepository voteRepository) {
        Vote voteOb = voteRepository.findVoteByUserEmailAndCommentId(vote.getUsername(), vote.getCommentId());

        if (voteOb != null) {
            if (voteOb.getVote() == 1)
                like -= 1;
            if (voteOb.getVote() == -1)
                dislike -= 1;
            voteRepository.deleteById(voteOb.getVoteID());
        }
        updateLikeDislike(vote);

    }

    private void updateLikeDislike(Vote vote) {
        if (vote.getVote() == 1)
            like += 1;
        if (vote.getVote() == -1)
            dislike += 1;
    }

    public ObjectNode getInformation(ObjectMapper mapper) {
        ObjectNode commentMapper = mapper.createObjectNode();
        commentMapper.put("commentId", commentId);
        commentMapper.put("userEmail", userEmail);
        commentMapper.put("text", text);
        commentMapper.put("like", like);
        commentMapper.put("dislike", dislike);
        return commentMapper;
    }

    public void checkForInvalidCommand() throws InvalidCommand {
        if (userEmail == null || commodityId == null || text == null || date == null)
            throw new InvalidCommand();
    }

    public int getCommentId() {
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

    public int getLike() {
        return like;
    }

    public int getDislike() {
        return dislike;
    }

}
