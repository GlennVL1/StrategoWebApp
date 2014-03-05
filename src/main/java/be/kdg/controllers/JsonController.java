package be.kdg.controllers;

import be.kdg.beans.DragDropBean;
import be.kdg.model.Achievement;
import be.kdg.model.User;
import be.kdg.service.api.AchievementServiceApi;
import be.kdg.service.api.UserServiceApi;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by wouter on 13/02/14.
 */
@Controller
public class JsonController {
    @Autowired
    private UserServiceApi userService;
    @Autowired
    private DragDropBean bean;

    private boolean zever = true;

    @Autowired
    private AchievementServiceApi achievementService;
    //todo behaalde achievemtns + alle achievements, getvriendenlijst

    @RequestMapping(value = "/api/verifyuser", method = RequestMethod.POST)
    @ResponseBody
    public String showData(@RequestParam("username")String username,@RequestParam("password")String password) throws JSONException {
        JSONObject jSonVerified = new JSONObject();
        jSonVerified.put("isVerified",userService.userIsValid(username,password));
        return jSonVerified.toString();
    }

    @RequestMapping(value = "/api/getachievements", method = RequestMethod.GET)
    @ResponseBody
    public String getAchievements(@RequestParam("username")String username){
        JSONObject resultObj = new JSONObject();
        try {
            List<Achievement> achievements = userService.getAchievementsByUsername(username);
            JSONArray array = new JSONArray();
            for(Achievement a : achievements) {
                JSONObject arrayElement = new JSONObject();
                arrayElement.put("id", a.getId());
                arrayElement.put("title", a.getTitle());
                arrayElement.put("description", a.getDescription());
                array.put(arrayElement);
            }
            resultObj.put("achievements", array);
            resultObj.put("username", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultObj.toString();
    }

    @RequestMapping(value = "api/getFriends", method = RequestMethod.GET)
    @ResponseBody
    public String getFriends(@RequestParam("username")String username) {
        JSONObject resultObj = new JSONObject();
        try{
            List<User> friends = userService.getFriendsByUsername(username);
            JSONArray array = new JSONArray();
            for(User friend : friends) {
                Boolean userAndFriendAreFriends = userService.userAndFriendAreFriends(username, friend.getUsername());
                JSONObject arrayElement = new JSONObject();
                arrayElement.put("id", friend.getId());
                arrayElement.put("email", friend.geteMail());
                arrayElement.put("username", friend.getUsername());
                arrayElement.put("status",friend.getStatus());
                arrayElement.put("status",friend.getStatus());
                arrayElement.put("userAndFriendAreFriends",userAndFriendAreFriends);
                array.put(arrayElement);
            }
            resultObj.put("friends", array);
            resultObj.put("username", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultObj.toString();
    }

    @RequestMapping(value = "api/game/setStartPosition", method = RequestMethod.GET)
    @ResponseBody
    public String setStartPosition(@RequestParam("pieces")String pieces ){

        bean.putStartPieces(pieces);
        boolean ready = bean.getReady();

        if(ready) {
            return "true";
        } else {
            return "false";
        }
    }

    @RequestMapping(value = "api/game/getReady", method = RequestMethod.GET)
    @ResponseBody
    public String getReady(){
        boolean ready = bean.getReady();

        if(ready) {
            return "true";
        } else {
            return "false";
        }
    }

    @RequestMapping(value = "api/game/setReady", method = RequestMethod.GET)
    @ResponseBody
    public String setReady(){
        bean.setReady();

        return "true";
    }

    @RequestMapping(value = "api/game/movePiece", method = RequestMethod.GET)
    @ResponseBody
    public String movePiece(@RequestParam("index")String index){
        int newIndex = Integer.parseInt(index.split(",")[0]);
        int oldIndex = Integer.parseInt(index.split(",")[1]);

        bean.movePiece(newIndex, oldIndex);

        return "true";
        //
    }

    /*@RequestMapping(value = "api/game/setstartposition", method = RequestMethod.GET)
    @ResponseBody
    public String setStartPosition(@RequestParam("gameid")int gameid, )
                                  */
    @RequestMapping(value = "api/game/setStartPosition", method = RequestMethod.POST)
    @ResponseBody
    public String setStartPosition(@RequestParam("pieces")String pieces,@RequestParam("username")String username ) throws JSONException {
        JSONObject jSonVerified = new JSONObject();
        if (pieces != null && !pieces.isEmpty() && username != null && !username.isEmpty()) {
            bean.putStartPieces(pieces);
            return jSonVerified.put("verified",true).toString();
        }
        return jSonVerified.put("verified",false).toString();


    }

    @RequestMapping(value = "api/logout",method = RequestMethod.POST)
    @ResponseBody
    public String logout(@RequestParam("username")String username) {
        userService.userLogout(username);
        return "true";
    }

    @RequestMapping(value = "api/addFriend",method = RequestMethod.POST)
    @ResponseBody
    public String addFriend(@RequestParam("username")String username,@RequestParam("friend")String friendname) {
       User friend =  userService.insertFriend(username,friendname);
        Boolean userAndFriendAreFriends = userService.userAndFriendAreFriends(username, friendname);
        JSONObject resultObject = new JSONObject();
        JSONObject friendObject = new JSONObject();
        try {
            friendObject.put("id",friend.getId());
            friendObject.put("email", friend.geteMail());
            friendObject.put("username", friend.getUsername());
            friendObject.put("status", friend.getStatus());
            friendObject.put("userAndFriendAreFriends",userAndFriendAreFriends);
            resultObject.put("friend",friendObject);
            resultObject.put("username",username);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultObject.toString();
    }
    /*
    @RequestMapping(value = "api/acceptInvite",method = RequestMethod.POST)
    @ResponseBody
    public String acceptInvite(@RequestParam("username")String username,@RequestParam("friend")String friendname){
        User friend = userService.acceptFriend(username,friendname);
    }*/
}