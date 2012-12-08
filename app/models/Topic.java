package models;

import javax.persistence.*;

import org.hibernate.annotations.Type;

import java.util.*;

import play.db.jpa.*;
import play.data.validation.*;

@Entity
public class Topic extends Model implements Comparable<Topic>{

    @Required
    public String subject;
    
    @Required
    public String sectionIdx;
    
    @Required
    public String problemIdx;
    
    @Type(type = "org.hibernate.type.TextType")
    @Lob
    @Required
    public String statement;
    
    public Integer views = 0;
    
    @ManyToOne
    public Forum forum;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "topic")
    public List<Post> posts;
    
    
    public Topic(Forum forum, User by, String sectionIdx, String problemIdx, String content) {
        this.forum = forum;        
        this.sectionIdx = sectionIdx;
        this.problemIdx = problemIdx;
        this.subject = String.format("%s.%s", sectionIdx, problemIdx);
        this.statement = content;
        create();
    }

        
    public Post reply(User by, String content) {
        return new Post(this, by, content);
    }
    
    
    public List<Post> getPosts(int page, int pageSize) {
        List<Post> list = Post.find("topic = ? order by likesNum desc, dislikesNum asc", this).fetch(page, pageSize);
        return list;
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
        if (!this.sectionIdx.equals(o.sectionIdx)) {
            return this.sectionIdx.compareTo(o.sectionIdx); 
        }
        else 
            return this.problemIdx.compareTo(o.problemIdx);
        
    }
    
}

