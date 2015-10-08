package com.psc.vote.controller;

import com.psc.vote.vote.service.VoteService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Controller
public class VoteController {

    @Autowired
    VoteService voteService;

    public static void main(String[] args) throws Exception {
        /*SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date campaignEndDate = dateFormat.parse("2015-08-10 14:58:00");
        long endDate = campaignEndDate.getTime();
        long sysDate = new Date().getTime();
        System.out.println("End Date:" + endDate);
        System.out.println("Current Date:" + sysDate);
        long diffMilliSeconds = endDate - sysDate;
        System.out.println("diffMilliSeconds:" + diffMilliSeconds);
        System.out.println("Seconds:" + diffMilliSeconds/1000);
        System.out.println("Minutes:" + diffMilliSeconds/(1000*60));
        System.out.println("Hours:" + diffMilliSeconds/(1000*60*60));
        System.out.println("Days:" + diffMilliSeconds/(1000*60*60*24));*/
        System.out.println("Password:" + ("testpassword").substring(0, 6));
    }

    @RequestMapping("/submitVote")
    @ResponseBody
    public String submitVote(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("in submitVote");
        String userName = request.getParameter("username");
        String optionId = request.getParameter("optionId");
        try {
            voteService.submitVote(userName, optionId);
            return "success";
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
            return "fail";
        }
    }


    @RequestMapping("/resubmitVote")
    @ResponseBody
    public String resubmitVote(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("in submitVote");
        String userName = request.getParameter("username");
        String optionId = request.getParameter("optionId");
        String oldOptionId = request.getParameter("oldOptionId");
        try {
            voteService.resubmitVote(userName, optionId, oldOptionId);
            return "success";
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
            return "fail";
        }
    }

    @RequestMapping("/displayStats")
    @ResponseBody
    public String displayStatistics(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("in submitVote");
        String campaignId = request.getParameter("campaignId");
        try {
            List<Map<String, String>> stats = voteService.displayStats(campaignId);
            JSONObject json = new JSONObject();
            int i = 0;
            JSONArray statsJSON = new JSONArray();
            for (Map<String, String> map : stats) {
                if (i == 0) {
                    json.put("client_name", map.get("client_name"));
                    json.put("anchor_id", map.get("anchor_id"));
                    json.put("campaign_id", map.get("campaign_id"));
                    json.put("question", map.get("question"));
                }
                JSONObject statJSON = new JSONObject();
                statJSON.put("age", map.get("age"));
                statJSON.put("cnt_value", map.get("cnt_value"));
                statsJSON.add(statJSON);
            }
            json.put("values", statsJSON.toJSONString());
            System.out.println("Stats:" + json.toString());
            return json.toString();
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
            return "fail";
        }
    }
}