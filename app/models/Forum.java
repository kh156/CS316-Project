package models;

import javax.persistence.*;

import org.hibernate.annotations.Type;

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
    
    @Type(type = "org.hibernate.type.TextType")
    @Lob
    public String description;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "forum")
    public List<Topic> topics;
    
    private int hashCode;    

    public Forum(String name, String ISBN, String author, String description) {
        update(name, ISBN, author, description);
        computeHashCode();
        create();
    }
    
    
    public void update(String name, String ISBN, String author, String description) {
        this.name = name;
        this.ISBN = ISBN;
        this.author = author;
        this.description = description;
    }
    
    
    public Topic newTopic(User by, String sectionIdx, String problemIdx, String content) {
        Topic t = new Topic(this, by, sectionIdx, problemIdx, content);
        this.refresh();
        return t;
    }
    
    
    public long getTopicsCount() {
        return Topic.count("forum", this);
    }

    public long getPostsCount() {
        return Post.count("topic.forum", this);
    }

    public List<Topic> getTopics(int page, int pageSize) {
        List<Topic> list = Topic.find("forum = ? order by sectionIdx, problemIdx", this).fetch(page, pageSize); 
//        Collections.sort(list);
        return list;
    }

    public Post getLastPost() {
        return Post.find("topic.forum = ? order by postedAt desc", this).first();
    }
    
    public boolean equals(Object o) {
    	if (o instanceof Forum) {
    		return o.hashCode() == hashCode;
    	}
    	return false;
    }
    
    private void computeHashCode() {
    	hashCode = Integer.parseInt(ISBN);
    }
    
    public int hashCode() {
    	return hashCode;
    }
}

