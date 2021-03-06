package controllers;

import play.data.validation.*;

import models.*;

public class Problems extends Application {

    public static void show(Long forumId, Long topicId, Integer page) {
        Problem topic = Problem.findById(topicId);
        notFoundIfNull(topic);
        topic.views += 1;
        topic.save();
        render(topic, page);
    }

    @Secure
    public static void post(Long forumId) {
        System.out.println("Problems.post: forumId is " + forumId);
        Textbook forum = Textbook.findById(forumId);
        notFoundIfNull(forum);
        render(forum);
    }
    
    @Secure(admin = true)
    public static void create(Long forumId, @Required String sectionIdx, @Required String problemIdx, @Required String content) {
        if (validation.hasErrors()) {
            validation.keep();
            params.flash();
            flash.error("Please correct these errors !");
            post(forumId);
        }
        Textbook forum = Textbook.findById(forumId);
        notFoundIfNull(forum);
        Problem newTopic = forum.newTopic(connectedUser(), sectionIdx, problemIdx, content);
        show(forumId, newTopic.id, null);
    }


    @Secure
    public static void reply(Long forumId, Long topicId) {
        Problem topic = Problem.findById(topicId);
        notFoundIfNull(topic);
        render(topic);
    }

    @Secure
    public static void createReply(Long forumId, Long topicId, @Required String content) {
        if (validation.hasErrors()) {
            validation.keep();
            params.flash();
            flash.error("Please correct these errors !");
            reply(forumId, topicId);
        }
        Problem topic = Problem.findById(topicId);
        notFoundIfNull(topic);
        topic.reply(connectedUser(), content);
        show(forumId, topicId, null);
    }

    @Secure(admin = true)
    public static void delete(Long forumId, Long topicId) {
        Problem topic = Problem.findById(topicId);
        notFoundIfNull(topic);
        topic.delete();
        flash.success("The topic has been deleted");
        Textbooks.show(forumId, null);
    }
    
    @Secure
    public static void likes(Long forumId, Long topicId, Long postId) {
        Post post = Post.findById(postId);
        post.like(connectedUser());
        post.save();
        show(forumId, topicId, null);
    }
    
    @Secure
    public static void dislikes(Long forumId, Long topicId, Long postId) {
        Post post = Post.findById(postId);
        post.dislike(connectedUser());
        post.save();
        show(forumId, topicId, null);
    }
}

