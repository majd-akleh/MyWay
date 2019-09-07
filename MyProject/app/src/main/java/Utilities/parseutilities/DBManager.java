package Utilities.parseutilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.user.myproject.R;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import Classes.Information;
import Classes.Post;
import Classes.Wish;
import Utilities.Verdict;


public class DBManager{
    Context applicationContext;
    private static ParseUser currentUser;
    private final String[] parseCodes = {"D5dHttOzaVfuy28uFTdCMiddfuiefTBnUsk4UbQJ","HH1XfsZsaauZPXjkJ8ldAma0b6ISgChipkzXRsSd"};
    public DBManager(Context applicationContext) {
        this.applicationContext = applicationContext;
    }
    public void initializeConnection(){
        Parse.initialize(applicationContext,parseCodes[0] ,parseCodes[1]);
    }

//--------------------------------------------------------------------------------------------------

    //general query methods:
    public ParseUser getCurrentUser(){
        return currentUser;
    }



 //--------------------------------------------------------------------------------------------------
    public Verdict existed(final String table,String... keyValuePairs) {
        final Verdict verdict = new Verdict(true, 0, "");
        StringBuilder colNames = new StringBuilder();
        for (int i = 1; i < keyValuePairs.length; i += 2) {
            colNames.append(keyValuePairs[i - 1]+" = "+keyValuePairs[i]);
            if (i < keyValuePairs.length - 1) colNames.append(", ");
        }

        if (table.equals("User")) {
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            for (int i = 1; i < keyValuePairs.length; i += 2) {
                String key = keyValuePairs[i - 1];
                String value = keyValuePairs[i];
                query.whereEqualTo(key, value);
            }

            try {
                int cnt = query.count();
                Log.e("existed", cnt + "");
                verdict.setOk(cnt > 0);
                verdict.setStatus(cnt);
                String tmp = colNames.toString() + (keyValuePairs.length == 2 ? " is " : " are ");
                if (cnt == 0) {
                    verdict.setDescription(tmp + "not existed");
                } else verdict.setDescription(tmp + "existed");

            } catch (ParseException e) {
                e.printStackTrace();
                Log.e("existed", e.toString());
            }
            return verdict;
        } else {
            ParseQuery query = ParseQuery.getQuery(table);
            for (int i = 1; i < keyValuePairs.length; i += 2) {
                String key = keyValuePairs[i - 1];
                String value = keyValuePairs[i];
                query.whereEqualTo(key, value);
            }

            try {
                int cnt = query.count();
                Log.e("existed", cnt + "");
                verdict.setOk(cnt > 0);
                verdict.setStatus(cnt);
                String tmp = colNames.toString() + (keyValuePairs.length == 2 ? " is " : " are ");
                if (cnt == 0) {
                    verdict.setDescription(tmp + "not used");
                } else verdict.setDescription(tmp + "used");

            } catch (ParseException e) {
                e.printStackTrace();
                Log.e("existed", e.toString());
            }
            return verdict;
        }
    }

    //--------------------------------------------------------------------------------------------------
    public Verdict notExisted(final String table,String... keyValuePairs){
        Verdict verdict = existed(table,keyValuePairs);
        verdict.setOk(!verdict.isOk());
        return verdict;
    }


    //--------------------------------------------------------------------------------------------------
    public String getUserID(String userName){
        ParseQuery<ParseUser> query = ParseQuery.getQuery("User");
        query.whereEqualTo("username",userName);
        try {
            ParseUser currentUser = query.getFirst();
            return currentUser.getObjectId();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    //--------------------------------------------------------------------------------------------------
    public void editCurrentUser(String... keyValuePairs){
        ParseUser user = ParseUser.getCurrentUser();
        for (int i = 1; i < keyValuePairs.length; i+=2) {
           if(keyValuePairs[i-1].equals("email"))user.setEmail(keyValuePairs[i]);
           else if(keyValuePairs.equals("password"))user.setPassword(keyValuePairs[i]);
           else user.put(keyValuePairs[i - 1], keyValuePairs[i]);
        }
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null)Log.e("debug",e.toString());
                else Log.d("done","edit user");
            }
        });
    }



    //--------------------------------------------------------------------------------------------------
    //login methods:
    public ParseException loginUser(Information info){
        try {
           ParseUser.logIn(info.getUserName(),info.getPassword());
        } catch (ParseException e) {
              return e;
        }
     return null;
    }

    //--------------------------------------------------------------------------------------------------
    public Verdict checkUser(Information info){
        Verdict verdict = notExisted("User", "username", info.getUserName());
        verdict.append(notExisted("User", "email", info.getEmail()));
        return verdict;
    }// not used



    //--------------------------------------------------------------------------------------------------
    //signUp methods
    public Verdict checkUserSignIn(Information info){
        return existed("User","username",info.getUserName(),"password",info.getPassword());
    }


    //--------------------------------------------------------------------------------------------------
    public ParseException addUser(Information info) {

     ParseUser user = new ParseUser();
     user.setUsername(info.getUserName());
     user.setPassword(info.getPassword());
     user.setEmail(info.getEmail());
     user.put("gender",info.getGender());
    // user.put("privacy",info.getPrivacy());
    // user.put("city",info.getCity());
        try {
            user.signUp();
            setNewPersonalImage(BitmapFactory.decodeResource(applicationContext.getResources(), R.drawable.user),ParseUser.getCurrentUser());

        } catch (ParseException e) {
            return e;
        }
        return null;
    }


    //--------------------------------------------------------------------------------------------------
    public ParseUser getParseUserByUserName(String userName){
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username",userName);
        try {
          return query.getFirst();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    //--------------------------------------------------------------------------------------------------
    public ParseFile getSavedParseFile(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        ParseFile file = new ParseFile("data",byteArray);
        try {
            file.save();
            return file;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    //--------------------------------------------------------------------------------------------------

    // general image handling methods:
    byte[] getBitmapBytes(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }


    //--------------------------------------------------------------------------------------------------
    public ParseException addImage(ParseUser user,String table,Bitmap bitmap){
        // this code is used to transform bitmap into byte array inorder to store it in parse DB
       byte[] byteArray = getBitmapBytes(bitmap);
        try {
            ParseFile file = new ParseFile("data",byteArray);
            file.save();
            ParseObject object = new ParseObject(table);
            object.put("parent",user);
            object.put("data",file);
            object.saveInBackground();
        } catch (ParseException e) {
            return e;
        }
    return null ;
    }



    //--------------------------------------------------------------------------------------------------
    public List<Bitmap> getAllUserBitmaps(ParseUser user,String table){
        ParseQuery<ParseObject> q = new ParseQuery<>(table);
        List<Bitmap> list;
        q.whereEqualTo("parent",user);
        try {
            List<ParseObject> all = q.find();
            list= new ArrayList<>();
            for (ParseObject o:all){
                byte[] bytes =  o.getParseFile("data").getData();
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                list.add(bitmap);
            }
            return list;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }



    //--------------------------------------------------------------------------------------------------

    //wish handling methods:
    public void addWish(Wish wish){
        ParseFile file = getSavedParseFile(wish.getBitmap());
        ParseObject object = new ParseObject("wish");
        object.put("parent",ParseUser.getCurrentUser());
        object.put("description",wish.getDescription());
        object.put("data",file);
        object.saveInBackground();
    }


    //--------------------------------------------------------------------------------------------------
    public List<Bitmap> getAllUserWishes(ParseUser user){
        ParseQuery<ParseObject> q = new ParseQuery<>("wish");
        List<Bitmap> list;
        q.whereEqualTo("parent",user);
        try {
            List<ParseObject> all = q.find();
            list= new ArrayList<>();
            for (ParseObject o:all){
                byte[] bytes =  o.getParseFile("data").getData();
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
             //TODO
              // String description = o.getString("description");
                list.add(bitmap);
            }
            return list;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }



    //--------------------------------------------------------------------------------------------------

    //following & followers methods:
    public void addFollowing(String followingUserName){
        ParseUser followerUser = ParseUser.getCurrentUser();
        ParseUser followingUser = getParseUserByUserName(followingUserName);
        ParseObject relation = new ParseObject("followingRelation");
        relation.put("follower",followerUser);
        relation.put("following",followingUser);
        relation.saveInBackground();
    }


    //--------------------------------------------------------------------------------------------------

    public List<ParseUser> getUserFollowings(ParseUser user){
        ParseQuery<ParseObject> q = ParseQuery.getQuery("followingRelation");
        q.whereEqualTo("following",user);
        try {
            ArrayList<ParseObject> o = (ArrayList<ParseObject>) q.find();
            ArrayList<ParseUser> ret = new ArrayList<>();

            for (ParseObject object : o) {
                ParseUser u = object.getParseUser("follower");
                ret.add(u.fetch());
            }
            return ret;
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("debug",e.toString());
        }
        return null;
    }



    //--------------------------------------------------------------------------------------------------
    public List<ParseUser> getUserFollowers(ParseUser user){
        ParseQuery<ParseObject> q = ParseQuery.getQuery("followingRelation");
        q.whereEqualTo("follower",user);
        try {
            ArrayList<ParseObject> o = (ArrayList<ParseObject>) q.find();
            ArrayList<ParseUser> ret = new ArrayList<>();

            for (ParseObject object : o) {
                ParseUser u = object.getParseUser("following");
                ret.add(u.fetch());
            }
            return ret;
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("debug",e.toString());
        }

        return null;
    }


    //--------------------------------------------------------------------------------------------------

  //personal Image methods:
    public void setNewPersonalImage(Bitmap bitmap,ParseUser user){
        ParseFile file = new ParseFile("data.png",getBitmapBytes(bitmap));
        try {
            file.save();
            ParseObject personalImage = new ParseObject("personalImage");
            personalImage.put("parent",user);
            personalImage.put("data",file);
            personalImage.saveInBackground();

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }



    //--------------------------------------------------------------------------------------------------
    public ParseException setPersonalImage(Bitmap bitmap) {
        ParseFile f = new ParseFile("data.png", getBitmapBytes(bitmap));

        try {
            f.save();
            ParseQuery parseQuery = ParseQuery.getQuery("personalImage");
            parseQuery.whereEqualTo("parent",ParseUser.getCurrentUser());
            ParseObject queryResult =parseQuery.getFirst();
            queryResult.delete();
            setNewPersonalImage(bitmap,ParseUser.getCurrentUser());
        } catch (ParseException e) {
            return e ;
        }
        return null;
    }



    //--------------------------------------------------------------------------------------------------
    public Bitmap getUserPersonalImage(ParseUser user){
          List<Bitmap> list = getAllUserBitmaps(user,"personalImage");
         if(list == null)return null;
          return list.get(0);
    }


    //--------------------------------------------------------------------------------------------------
    public Bitmap getPersonalImage(){
        return getUserPersonalImage(ParseUser.getCurrentUser());
    }



    //--------------------------------------------------------------------------------------------------
   //search methods:
    public List<ParseUser> searchPeople(String searchPattern){

        try {
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereEqualTo("username",  searchPattern );
            HashSet<ParseUser> ret = new HashSet<>();
            ret.addAll(query.find());

//            query = ParseUser.getQuery();
//            query.whereEqualTo("city",searchPattern);
//            ret.addAll(query.find());

            return new ArrayList<>(ret);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    //--------------------------------------------------------------------------------------------------
    //post methods:
    public int getNextID(){
        // you have to create class called "postID" with one column called "value" have value 0

        ParseQuery<ParseObject> q = ParseQuery.getQuery("postID");

        try {

               ParseObject o =  q.getFirst();
               int v = o.getInt("value");
               o.put("value",v+1);
               o.saveInBackground();
               return v;
        } catch (ParseException e) {
            //Log.e("debug",e.toString());
        }
        return -1;
    }



    //--------------------------------------------------------------------------------------------------
    public ParseObject getPostObjectByID(int postID){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
        query.whereEqualTo("ID",postID);
        try {
            return query.getFirst();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }





    //--------------------------------------------------------------------------------------------------

    public ParseException addPost(Post post,ParseUser poster){
        byte[] byteArray = null;
        ParseFile file = null ;
       if(post.getImage() !=null) byteArray = getBitmapBytes(post.getImage());
        try {
            if(post.getImage() !=null)
            { file = new ParseFile("data", byteArray);
            file.save();
            }
            ParseObject postObject = new ParseObject("Post");
            postObject.put("ID", post.getID());
            postObject.put("username", post.getUsername());
            postObject.put("date", post.getDate());
            postObject.put("description", post.getDescription());
            postObject.put("likes", post.getLikes());
            postObject.put("dislikes", post.getDislikes());
            postObject.put("price", post.getPrice());
            postObject.put("rating", post.getRating());
            postObject.put("season", post.getSeason());
            if(post.getImage() !=null)   postObject.put("image",file);
            postObject.put("parent",poster);
            postObject.save();
        } catch (ParseException e) {
           return e ;
        }
        return null;
    }


    //--------------------------------------------------------------------------------------------------
    public ArrayList<Classes.Comment> getPostComments(ParseObject postObject,Date filter){
        ParseQuery<ParseObject> query =  ParseQuery.getQuery("comments");
       // query.whereGreaterThanOrEqualTo("date",filter);
        query.whereEqualTo("parent",postObject);
        List<ParseObject> list = null;
        try {
            list = query.find();
            List<Classes.Comment> ret = new ArrayList<>();
            for (ParseObject object : list) {
                ret.add(new Classes.Comment(object.getString("commenter"),object.getString("description"),object.getDate("date")));
            }
            return new ArrayList<>(ret);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    //--------------------------------------------------------------------------------------------------
    public void addComment(Classes.Comment comment,ParseUser commenter,int postID){
        ParseObject parentPost = getPostObjectByID(postID);
        if (parentPost == null){
            Log.e("debug","wrong post ID in addComment()");
            return;
        }
        ParseObject parseObject = new ParseObject("comments");
        parseObject.put("description",comment.getDescription());
        parseObject.put("data",comment.getDate());
        parseObject.put("commenter",comment.getCommenter());
        parseObject.put("parent",parentPost);
        parseObject.saveInBackground();
    }




    //--------------------------------------------------------------------------------------------------
    public List<Post> getUserPosts(ParseUser poster,int limit){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
        query.setLimit(limit);
        query.whereEqualTo("parent",poster);
        try {

            List<ParseObject> list = query.find();
            List<Post> ret = new ArrayList<>();
            for (ParseObject object : list) {
                ret.add(makePost(object));
            }
            return ret;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }



    //--------------------------------------------------------------------------------------------------
    public Post  makePost(ParseObject post){
        Post ret = new Post();
        ret.setSeason(post.getString("season"));
        ret.setDescription(post.getString("description"));
        ret.setPrice(post.getInt("price"));
        ret.setRating(post.getNumber("rating"));
        ret.setID(post.getInt("ID"));
        ret.setUsername(post.getString("username"));
        ret.setDate(post.getDate("date"));
        ret.setLikes(post.getInt("likes"));
        ret.setDislikes(post.getInt("dislikes"));
        ParseFile imageFile = post.getParseFile("image");
        byte[] bytes = new byte[0];
        try {
            if(post.containsKey( "image" )) {
                bytes = imageFile.getData();
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                ret.setImage(bitmap);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ret.setComments(getPostComments(post, ret.getDate()));
        return ret;
    }




    //--------------------------------------------------------------------------------------------------
    public List<Post> getTimeLine(ParseUser user){
        List<ParseUser> fs = getUserFollowers(user);
        List<Post> ret = new ArrayList<>();
        for (ParseUser f : fs) {
            ret.addAll(getUserPosts(f,10));
        }
        ret.addAll( getUserPosts(user ,10) );
        return ret;
    }



    //--------------------------------------------------------------------------------------------------
    public boolean checkFollowingRelation(ParseUser follower, ParseUser following){
        ParseQuery<ParseObject> q = ParseQuery.getQuery("followingRelation");
        q.whereEqualTo("following",following);
        q.whereEqualTo("follower",follower);
        try {
            return q.count()>0;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }



    //--------------------------------------------------------------------------------------------------
    // this method has changed on 27/3/2015
    public void addLike(ParseUser user,int postID){
        ParseObject parseObject = getPostObjectByID(postID);

        if (parseObject==null){
            Log.e("debug","wrong post ID in add like");
            return;
        }

        int likes = parseObject.getInt("likes");
        parseObject.put("likes",likes+1);
        parseObject.saveInBackground();

        ParseObject userLikes = new ParseObject("usersLikes");
        userLikes.put("parent",user);
        userLikes.put("post",postID);
        userLikes.saveInBackground();
    }



    //--------------------------------------------------------------------------------------------------
    // this method has changed on 27/3/2015
    public boolean userLikedPost(ParseUser user,int postID){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("usersLikes");
        query.whereEqualTo("parent",user);
        query.whereEqualTo("post",postID);
        try {
            return query.count() > 0;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }


    //--------------------------------------------------------------------------------------------------
    // this method has changed on 27/3/2015
    public void addDislike(ParseUser user,int postID){
        ParseObject parseObject = getPostObjectByID(postID);
        if (parseObject==null){
            Log.e("debug","wrong post ID in add like");
            return;
        }
        int dislikes = parseObject.getInt("dislikes");
        parseObject.put("dislikes",dislikes+1);
        parseObject.saveInBackground();

        ParseObject userDislikes = new ParseObject("usersDislikes");
        userDislikes.put("parent",user);
        userDislikes.put("post",postID);
        userDislikes.saveInBackground();

    }



    //--------------------------------------------------------------------------------------------------
    // this method has changed on 27/3/2015
    public boolean userDislikedPost(ParseUser user,int postID){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("usersDislikes");
        query.whereEqualTo("parent",user);
        query.whereEqualTo("post",postID);
        try {
            return query.count() > 0;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }


}
