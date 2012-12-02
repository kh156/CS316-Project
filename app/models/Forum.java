package models;

import javax.persistence.*;
import java.util.*;

import play.db.jpa.*;
import play.data.validation.*;

@Entity
public class Forum extends Model {

    @Required
    public String name;
    
    @Required
    public String ISBN;
    
    @Required
    public String author;
    
    @MaxSize(255)
    public String description;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "forum")
    public List<Topic> topics;
    
    // ~~~~~~~~~~~~ 
    
//    public Forum(String name, String description) {
//        this.name = name;
//        this.description = description;
//        create();
//    }

    public Forum(String name, String ISBN, String author, String description) {
        update(name, ISBN, author, description);
        create();
    }
    
    // ~~~~~~~~~~~~ 
    public void update(String name, String ISBN, String author, String description) {
        this.name = name;
        this.ISBN = ISBN;
        this.author = author;
        this.description = description;
    }
    
    // ~~~~~~~~~~~~ 
    
//    public Topic newTopic(User by, String subject, String content) {
//        Topic t = new Topic(this, by, subject, content);
//        this.refresh();
//        return t;
//    }
    
    public Topic newTopic(User by, int sectionIdx, int problemIdx, String content) {
        Topic t = new Topic(this, by, sectionIdx, problemIdx, content);
        this.refresh();
        return t;
    }
    
    // ~~~~~~~~~~~~ 
    
    public long getTopicsCount() {
        return Topic.count("forum", this);
    }

    public long getPostsCount() {
        return Post.count("topic.forum", this);
    }

    public List<Topic> getTopics(int page, int pageSize) {
        List<Topic> list = Topic.find("forum", this).fetch(page, pageSize); 
        Collections.sort(list);
        return list;
    }

    public Post getLastPost() {
        return Post.find("topic.forum = ? order by postedAt desc", this).first();
    }
    
}

