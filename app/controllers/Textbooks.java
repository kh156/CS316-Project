package controllers;

import java.util.*;

import play.data.validation.*;
import search.SearchUtils;

import models.*;

public class Textbooks extends Application {

    public static void index(Integer page) { 
        if (page == null) page = 1;
        List forums = Textbook.find("ISBN > ?", "").fetch(page, pageSize);
        long textbooksCount = Textbook.count();
        render(forums, textbooksCount, page);
    }
    
    public static void results(String query, String category) {
    	List forums = new ArrayList();
    	if (category.equals("ISBN")) {
    		forums = Textbook.find("byISBN", query).fetch();
    		render(forums);
    		return;
    	} else {
    		String[] keywords = query.split("\\s+");
    		for (String key : keywords) {
    			forums.addAll(Textbook.find("by" + category + "Like", "%" + key + "%").fetch());
    		}
    	}    	    	    	
    	forums = SearchUtils.sortByFreqs(forums);
    	render(forums);
    }

    @Secure(admin = true)
    public static void create(@Required String name, @Required String ISBN, @Required String author, @MaxSize(800) String description) {
        if (validation.hasErrors()) {
            validation.keep();
            params.flash();
            flash.error("Please correct these errors !");
            index(null);
        }
        Textbook forum = new Textbook(name, ISBN, author, description);
        forum.save();
        index(null);
    }

    @Secure(admin = true)
    public static void update(Long forumId, @Required String name, @Required String ISBN, @Required String author, @MaxSize(800) String description) {
        if (validation.hasErrors()) {
            validation.keep();
            params.flash();
            flash.error("Please correct these errors !");
            edit(forumId);
        }
        Textbook forum = Textbook.findById(forumId);
        forum.update(name, ISBN, author, description);
        forum.save();
        index(null);
    }
    
    public static void show(Long forumId, Integer page) {
        Textbook forum = Textbook.findById(forumId);
        notFoundIfNull(forum);
        render(forum, page);
    }

    @Secure(admin = true)
    public static void delete(Long forumId) {
        Textbook forum = Textbook.findById(forumId);
        notFoundIfNull(forum);
        forum.delete();
        flash.success("The forum has been deleted");
        index(null);
    }
    
    @Secure(admin = true)
    public static void edit(Long forumId) {
        System.out.println("Test.edit: forumId is " + forumId);
        Textbook forum = Textbook.findById(forumId);
        render(forum);
    }
}