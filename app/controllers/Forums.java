package controllers;

import java.util.*;

import play.data.validation.*;

import models.*;

public class Forums extends Application {

    public static void index() { 
        List forums = Forum.findAll();
        long topicsCount = Topic.count();
        long postsCount = Post.count();
        render(forums, topicsCount, postsCount);
    }
    
    public static void results(String query, String category) {
    	Set s = new HashSet();
    	if (category.equals("ISBN")) {
    		List forums = Forum.find("byISBN", query).fetch();
    		render(forums);
    		return;
    	} else {
    		String[] keywords = query.split("\\s+");
    		for (String key : keywords) {
    			s.addAll(Forum.find("by" + category + "Like", "%" + key + "%").fetch());
    		}
    	} 
    	List forums = new ArrayList(s);
    	render(forums);
    }

    @Secure(admin = true)
    public static void create(@Required String name, @Required String ISBN, @Required String author, @MaxSize(500) String description) {
        if (validation.hasErrors()) {
            validation.keep();
            params.flash();
            flash.error("Please correct these errors !");
            index();
        }
        Forum forum = new Forum(name, ISBN, author, description);
        forum.save();
        index();
    }

    @Secure(admin = true)
    public static void update(Long forumId, @Required String name, @Required String ISBN, @Required String author, @MaxSize(500) String description) {
        if (validation.hasErrors()) {
            validation.keep();
            params.flash();
            flash.error("Please correct these errors !");
            index();
        }
        Forum forum = Forum.findById(forumId);
        forum.update(name, ISBN, author, description);
        forum.save();
        index();
    }
    
    public static void show(Long forumId, Integer page) {
        Forum forum = Forum.findById(forumId);
        notFoundIfNull(forum);
        render(forum, page);
    }

    @Secure(admin = true)
    public static void delete(Long forumId) {
        Forum forum = Forum.findById(forumId);
        notFoundIfNull(forum);
        forum.delete();
        flash.success("The forum has been deleted");
        index();
    }
    
    @Secure(admin = true)
    public static void edit(Long forumId) {
        Forum forum = Forum.findById(forumId);
        render(forum);
    }
}