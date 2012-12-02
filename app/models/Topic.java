package models;

import javax.persistence.*;
import java.util.*;

import play.db.jpa.*;
import play.data.validation.*;

@Entity
public class Topic extends Model implements Comparable<Topic>{

    @Required
    public String subject;
    
    @Required
    public int sectionIdx;
    
    @Required
    public int problemIdx;
    
    public String statement;
    
    public Integer views = 0;
    
    @ManyToOne
    public Forum forum;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "topic")
    public List<Post> posts;
    
    // ~~~~~~~~~~~~ 
    
//    public Topic(Forum forum, User by, String subject, String content) {
//        this.forum = forum;
//        this.subject = subject;
//        
//        this.sectionIdx = Integer.parseInt(subject.split("/")[0]);
//        this.problemIdx = Integer.parseInt(subject.split("/")[1]);
//        
//        create();
//        new Post(this, by, content);
//    }
    
    public Topic(Forum forum, User by, int sectionIdx, int problemIdx, String content) {
        this.forum = forum;        
        this.sectionIdx = sectionIdx;
        this.problemIdx = problemIdx;
        this.subject = String.format("%d.%d", sectionIdx, problemIdx);
        this.statement = content;
        create();
    }

    
    // ~~~~~~~~~~~~ 
    
    public Post reply(User by, String content) {
        return new Post(this, by, content);
    }
    
    // ~~~~~~~~~~~~ 
    
    public List<Post> getPosts(int page, int pageSize) {
        return Post.find("topic", this).fetch(page, pageSize);
    }

    public Long getPostsCount() {
        return Post.count("topic", this);
    }

    public Long getVoicesCount() {
        return User.count("select count(distinct u) from User u, Topic t, Post p where p.postedBy = u and p.topic = t and t = ?1", this);
    }

    public Post getLastPost() {
        return Post.find("topic = ? order by postedAt desc", this).first();
    }

    @Override
    public int compareTo(Topic o) {
        if (this.sectionIdx != o.sectionIdx) {
            return (this.sectionIdx < o.sectionIdx) ? -1 : 1; 
        }
        else 
            return (this.problemIdx < o.problemIdx) ? -1 : 1;
        
    }
    
}

