package models;

import javax.persistence.*;

import org.hibernate.annotations.Type;

import java.util.*;

import play.db.jpa.*;
import play.data.validation.*;

@Entity
public class Textbook extends Model {

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
    public List<Problem> topics;
    
    private int hashCode;    

    public Textbook(String name, String ISBN, String author, String description) {
        update(name, ISBN, author, description);
        create();
    }
    
    
    public void update(String name, String ISBN, String author, String description) {
        this.name = name;
        this.ISBN = ISBN;
        this.author = author;
        this.description = description;        
    }
    
    
    public Problem newTopic(User by, String sectionIdx, String problemIdx, String content) {
        Problem t = new Problem(this, by, sectionIdx, problemIdx, content);
        this.refresh();
        return t;
    }
    
    
    public long getTopicsCount() {
        return Problem.count("forum", this);
    }

    public long getPostsCount() {
        return Post.count("topic.forum", this);
    }

    public static List<Textbook> getTextbooks(int page, int pageSize) {
        List<Textbook> list = Textbook.find("ISBN > ?", "").fetch(page, pageSize);
        return list;
    }
    
    public List<Problem> getTopics(int page, int pageSize) {
        List<Problem> list = Problem.find("forum = ? order by sectionIdx, problemIdx", this).fetch(page, pageSize); 
//        Collections.sort(list);
        return list;
    }

    public Post getLastPost() {
        return Post.find("topic.forum = ? order by postedAt desc", this).first();
    }
    
    public boolean equals(Object o) {
    	if (o instanceof Textbook) {
    		return o.hashCode() == hashCode;
    	}
    	return false;
    }   
    
    public int hashCode() {
    	return ISBN.hashCode();
    }
}

