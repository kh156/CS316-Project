package models;

import javax.persistence.*;

import java.util.*;

import play.data.binding.*;
import play.data.validation.MaxSize;
import play.data.validation.Required;

import play.db.jpa.*;

@Entity
public class Post extends Model{

    @Required
    @Lob
    public String content;

    @As("yyyy-MM-dd")
    public Date postedAt;

    @OneToOne
    public User postedBy;

    @ManyToOne
    public Topic topic;

    private HashSet<Long> likes;

    private HashSet<Long> dislikes;
  
    public int likesNum;
    
    public int dislikesNum;
    // ~~~~~~~~~~~~ 

    public Post(Topic topic, User postedBy, String content) {
        this.topic = topic;
        this.postedBy = postedBy;
        this.content = content;
        this.postedAt = new Date();
        
        likes = new HashSet<Long>();
        dislikes = new HashSet<Long>();
        create();
    }

    private void checkInitialized() {
        if (likes == null) 
            likes = new HashSet<Long>();
        if (dislikes == null) 
            dislikes = new HashSet<Long>();
    }
    
    public void like(User user) {
        checkInitialized();
        dislikes.remove(user.id);
        if (!likes.contains(user.id)) {
            likes.add(user.id);
        }
        likesNum = likes.size();
        dislikesNum = dislikes.size();
    }
    
    public void dislike(User user) {
        checkInitialized();
        likes.remove(user.id);
        if (!dislikes.contains(user.id)) {
            dislikes.add(user.id);
        }
        likesNum = likes.size();
        dislikesNum = dislikes.size();
    }

}

