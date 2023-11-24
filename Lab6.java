package lab6;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

class User 
{
    public String userId;
    public String username;
    public String password;
    public String email;
    public List<Post> posts;
    public User(String userId, String username, String password, String email) 
    {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.posts = new ArrayList<>();
    }
    public void editProfile(String username, String email) 
    {
        this.username = username;
        this.email = email;
    }
    public String getUserId() 
    {
        return userId;
    }
    public String getUsername()
    {
        return username;
    }
    public String getPassword() 
    {
        return password;
    }
    public String getEmail() 
    {
        return email;
    }
    public List<Post> getPosts()
    {
        return posts;
    }
    public void saveUserProfile() 
    {
        try (PrintWriter writer = new PrintWriter("user_profiles.txt")) {
            writer.println(userId);
            writer.println(username);
            writer.println(password);
            writer.println(email);
        } catch (FileNotFoundException e) 
        {
            System.err.println("Error saving user profile: " + e.getMessage());
        }
    }
    public static User loadUserProfile(String userId) throws FileNotFoundException, IOException 
    {
        try (BufferedReader reader = new BufferedReader(new FileReader("user_profiles.txt")))
        {
            String loadedUserId = reader.readLine();
            String loadedUsername = reader.readLine();
            String loadedPassword = reader.readLine();
            String loadedEmail = reader.readLine();

            return new User(loadedUserId, loadedUsername, loadedPassword, loadedEmail);
        } catch (FileNotFoundException e) {
            System.err.println("Error loading user profile: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error reading user profile: " + e.getMessage());
        }
        return null;
    }
}

class Post 
{
    public String postId;
    public String caption;
    public User user;
    public List<Comment> comments;
    public Post(String postId, String caption, User user) {
        this.postId = postId;
        this.caption = caption;
        this.user = user;
        this.comments = new ArrayList<>();
    }
    public void editCaption(String caption) {
        this.caption = caption;
    }
    public String getPostId() {
        return postId;
    }
    public String getCaption() {
        return caption;
    }
    public User getUser() {
        return user;
    }
    public List<Comment> getComments() {
        return comments;
    }
    public void savePost() {
        try (PrintWriter writer = new PrintWriter("posts.txt")) {
            writer.println(postId);
            writer.println(caption);
        } catch (FileNotFoundException e) {
            System.err.println("Error saving post: " + e.getMessage());
        }
    }
    public static Post loadPost(String postId) {
        try (BufferedReader reader = new BufferedReader(new FileReader("posts.txt")))
        {
            String loadedPostId = reader.readLine();
            String loadedCaption = reader.readLine();
            return new Post(loadedPostId,loadedCaption,);
        } catch (FileNotFoundException e) {
            System.err.println("Error loading post: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error reading post: " + e.getMessage());
        }
        return null;
    }
}
class Comment {
    public String commentId;
    public String text;
    public User user;

    public Comment(String commentId, String text, User user) {
        this.commentId = commentId;
        this.text = text;
        this.user = user;
    }
    public String getCommentId() {
        return commentId;
    }
    public String getText() {
        return text;
    }
    public User getUser() {
        return user;
    }
    public void saveComment() {
        try (PrintWriter writer = new PrintWriter("comments.txt")) {
            writer.println(commentId);
            writer.println(text);
            
        } catch (FileNotFoundException e) {
            System.err.println("Error saving comment: " + e.getMessage());
        }
    }
    public static Comment loadComment(String commentId) {
        try (BufferedReader reader = new BufferedReader(new FileReader("comments.txt")))
        {
            String loadedCommentId = reader.readLine();
            String loadedText = reader.readLine();
            
        } catch (FileNotFoundException e) {
            System.err.println("Error loading comment: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error reading comment: " + e.getMessage());
        }
        return null;
    }
}

class App {
    public List<User> users;
    public List<Post> posts;

    public App() {
        this.users = new ArrayList<>();
        this.posts = new ArrayList<>();
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void removeUser(User user) {
        users.remove(user);
    }

    public void createPost(User user, Post post) {
        user.getPosts().add(post);
        posts.add(post);
    }

    public void deletePost(Post post) {
        User user = post.getUser();
        user.getPosts().remove(post);
        posts.remove(post);
    }

    public void addComment(Post post, Comment comment) {
        post.getComments().add(comment);
    }

    public void deleteComment(Comment comment) {
        Post post = getPostContainingComment(comment);
        if (post != null) {
            post.getComments().remove(comment);
        }
    }

    private Post getPostContainingComment(Comment comment) {
        for (Post post : posts) {
            if (post.getComments().contains(comment)) {
                return post;
            }
        }
        return null;
    }
}

public class Lab6 {
    public static void main(String[] args) 
    {
        App app = new App();

        // Task 5-a: Create a user
        User user1 = new User("1", "user1", "password1", "user1@example.com");
        app.addUser(user1);
        System.out.println("User created: " + user1.getUsername());

        // Task 5-b: Create a post
        Post post1 = new Post("101", "Caption 1", user1);
        app.createPost(user1, post1);
        System.out.println("Post created by " + user1.getUsername() + ": " + post1.getCaption());

        // Task 5-c: Add a comment to a post
        Comment comment1 = new Comment("201", "Nice post!", user1);
        app.addComment(post1, comment1);
        System.out.println("Comment added by " + user1.getUsername() + " to post: " + comment1.getText());

        // Task 5-d: Delete a post
        try 
        {
            app.deletePost(post1);
            System.out.println("Post deleted: " + post1.getCaption());
        } catch (Exception e) {
            System.err.println("Error deleting post: " + e.getMessage());
        }

        // Task 5-e: Exception handling use-cases
        try 
        {
            User loadedUser = User.loadUserProfile("2");
            if (loadedUser != null) {
                System.out.println("Loaded user: " + loadedUser.getUsername());
            } else {
                System.out.println("User not found.");
            }
        } catch (IOException e) {
            System.err.println("Error loading user profile: " + e.getMessage());
        }

        try 
        {
            Post loadedPost = Post.loadPost("102");
            if (loadedPost != null) {
                System.out.println("Loaded post caption: " + loadedPost.getCaption());
            } else {
                System.out.println("Post not found.");
            }
        } catch (Exception e) {
            System.err.println("Error loading post: " + e.getMessage());
        }

        try 
        {
            Comment loadedComment = Comment.loadComment("202");
            if (loadedComment != null) 
            {
                System.out.println("Loaded comment text: " + loadedComment.getText());
            } else {
                System.out.println("Comment not found.");
            }
        } catch (Exception e) {
            System.err.println("Error loading comment: " + e.getMessage());
        }
    }
}

       
