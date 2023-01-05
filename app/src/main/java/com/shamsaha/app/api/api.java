package com.shamsaha.app.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.shamsaha.app.ApiModel.ChannelListModel;
import com.shamsaha.app.ApiModel.ChannelStatusModel;
import com.shamsaha.app.ApiModel.GetInvolved;
import com.shamsaha.app.ApiModel.GoogleForm;
import com.shamsaha.app.ApiModel.IndidualSponsor;
import com.shamsaha.app.ApiModel.ModelToken;
import com.shamsaha.app.ApiModel.PinModel;
import com.shamsaha.app.ApiModel.PublicPart.AboutModel.BordMembers;
import com.shamsaha.app.ApiModel.PublicPart.AboutModel.Partner;
import com.shamsaha.app.ApiModel.PublicPart.AboutModel.Who_we_are;
import com.shamsaha.app.ApiModel.ResourceCategory;
import com.shamsaha.app.ApiModel.ResponseBody;
import com.shamsaha.app.ApiModel.SurvivorModel;
import com.shamsaha.app.ApiModel.Termsncondition;
import com.shamsaha.app.ApiModel.Victem.CaseModel;
import com.shamsaha.app.ApiModel.WorkwithUS;
import com.shamsaha.app.ApiModel.advocacy;
import com.shamsaha.app.ApiModel.applogo;
import com.shamsaha.app.ApiModel.contact;
import com.shamsaha.app.ApiModel.corprateSponsor;
import com.shamsaha.app.ApiModel.event;
import com.shamsaha.app.ApiModel.event_registration_api_adaptor;
import com.shamsaha.app.ApiModel.job;
import com.shamsaha.app.ApiModel.resource;
import com.shamsaha.app.ApiModel.resource_location;
import com.shamsaha.app.ApiModel.volunteering;
import com.shamsaha.app.ApiModel.volunter.AdditionalLanguageModel;
import com.shamsaha.app.ApiModel.volunter.Announcement;
import com.shamsaha.app.ApiModel.volunter.HelpRequestModel;
import com.shamsaha.app.ApiModel.volunter.LanguageRequestModel;
import com.shamsaha.app.ApiModel.volunter.MessageModel;
import com.shamsaha.app.ApiModel.volunter.ModModel;
import com.shamsaha.app.ApiModel.volunter.MyShift;
import com.shamsaha.app.ApiModel.volunter.OnDateShift;
import com.shamsaha.app.ApiModel.volunter.OpenShift;
import com.shamsaha.app.ApiModel.volunter.ProfileInfoModel;
import com.shamsaha.app.ApiModel.volunter.ProfilePic;
import com.shamsaha.app.ApiModel.volunter.ScheduleStatusModel;
import com.shamsaha.app.ApiModel.volunter.ShiftRequestMode;
import com.shamsaha.app.ApiModel.volunter.UpcomingShift;
import com.shamsaha.app.ApiModel.volunter.UpdateprofileModel;
import com.shamsaha.app.activity.general.Credentials;
import com.shamsaha.app.adaptor.volunteer_login;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface api {

    @POST("Logo")
    Call<List<applogo>> getLogo();

    @POST("Contactus")
    Call<List<contact>> getContactData();

    @POST("Con_message")
    @FormUrlEncoded
    Call<MessageModel> Con_message(@Field("name") String name, @Field("email") String email, @Field("message") String message);


    @POST("home")
    Call<List<Who_we_are>> getAboutData();

    @POST("Get_involved")
    Call<List<GetInvolved>> getInvolvedData();

    @POST("vgetinvolved")
    Call<List<volunteering>> volunteeringData();

    @POST("resource_location")
    Call<List<resource_location>> getResource();

    @POST("victim_event")
    Call<List<event>> event();

    @POST("volunteer_event")
    Call<List<event>> volunteerEvent();

    @POST("volunteer_event")
    Call<MessageModel> volunteerEventMessage();

    @POST("Partner")
    Call<List<Partner>> Partner();

    @POST("Bmember")
    Call<List<BordMembers>> Bmember();

    @POST("event_registration")
    @FormUrlEncoded
    Call<event_registration_api_adaptor> event_registration(@Field("name") String name,
                                                            @Field("email") String email,
                                                            @Field("phone") String phone,
                                                            @Field("address") String address,
                                                            @Field("event_id") String event_id,
                                                            @Field("amount") String amount);


    @POST("resource_category")
    @FormUrlEncoded
    Call<List<ResourceCategory>> resource_category(@Field("location") String location_id);

    @POST("resource")
    @FormUrlEncoded
    Call<List<resource>> resourceContact(@Field("location") String location_id,
                                         @Field("category") String category);

    @POST("Checkdevice")
    @FormUrlEncoded
    Call<MessageModel> Checkdevice(@Field("deviceid") String deviceid);

    @POST("termsnconditions")
    Call<List<Termsncondition>> termsnconditionsData();

    @POST("about")
    Call<List<Who_we_are>> aboutData();

    @POST("advocacy")
    Call<List<advocacy>> getForm(@Body Credentials credentials);

    @POST("workwithus")
    Call<List<WorkwithUS>> workwithusContent();

    @POST("job")
    Call<List<job>> jobList();

    @POST("sgetinvolved")
    Call<List<IndidualSponsor>> sgetinvolved();

    @POST("scgetinvolved")
    Call<List<corprateSponsor>> scgetinvolved();


    @Multipart
    @POST("jobapply")
    Call<ResponseBody> editUser(@Part MultipartBody.Part ufile,
                                @Part("name") RequestBody name,
                                @Part("email") RequestBody email,
                                @Part("phone") RequestBody phone,
                                @Part("address") RequestBody address,
                                @Part("statement") RequestBody statement,
                                @Part("jobid") RequestBody jobid);

    @Multipart
    @POST("jobapply")
    Call<ResponseBody> other(
            @Part("name") RequestBody name,
            @Part("email") RequestBody email,
            @Part("mobile") RequestBody phone,
            @Part("address") RequestBody address,
            @Part("intrest") RequestBody statement,
            @Part MultipartBody.Part ufile);

    @Multipart
    @POST("vol_contact")
    Call<ResponseBody> vol_contact(
            @Part("name") RequestBody name,
            @Part("email") RequestBody email,
            @Part("mobile") RequestBody phone,
            @Part("address") RequestBody address,
            @Part("intrest") RequestBody statement,
            @Part MultipartBody.Part ufile);


    @POST("advocacy")
    @FormUrlEncoded
    Call<GoogleForm> GoogleForm(@Field("email_id") String email_id,
                                @Field("fullname") String fullname,
                                @Field("mobile") String mobile,
                                @Field("age_above_r_nt") String age_above_r_nt,
                                @Field("language_u_speak") String language_u_speak,
                                @Field("transportation") String transportation,
                                @Field("stay_in") String stay_in,
                                @Field("attend_training") String attend_training,
                                @Field("r_u_volunteer") String r_u_volunteer,
                                @Field("unpain_volunteer") String unpain_volunteer,
                                @Field("traning_fee") String traning_fee,
                                @Field("any_additional_skill") String any_additional_skill,
                                @Field("understand_r_not") String understand_r_not);


    @POST("register_victim")
    @FormUrlEncoded
    Call<MessageModel> register_victim(
            @Field("deviceid") String deviceid,
            @Field("pin") String pin,
            @Field("conf_pin") String conf_pin,
            @Field("name") String name,
            @Field("email") String email,
            @Field("mobile") String mobile,
            @Field("address") String address,
            @Field("nationality") String nationality);


    @POST("changepin_victim")
    @FormUrlEncoded
    Call<MessageModel> changepinVictim(
            @Field("deviceid") String deviceid,
            @Field("pin") String pin);


    @POST("sponsership_contact/add")
    @FormUrlEncoded
    Call<MessageModel> sponsershipContact(
            @Field("name") String name,
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("company") String company,
            @Field("message") String message);

    @POST("checkdevice_victim")
    @FormUrlEncoded
    Call<MessageModel> checkdeviceVictim(
            @Field("deviceid") String deviceid);


    @POST("checkvictim")
    @FormUrlEncoded
    Call<PinModel> checkvictim(
            @Field("deviceid") String deviceid,
            @Field("pin") String pin);

    @POST("forgetpin_victim")
    @FormUrlEncoded
    Call<MessageModel> forgetPINVictim(
            @Field("deviceid") String deviceid);


    //Session 3
    @POST("volunteer_login")
    @FormUrlEncoded
    Call<volunteer_login> login(@Field("email") String email,
                                @Field("password") String password,
                                @Field("deviceid") String deviceid,
                                @Field("tokenid") String tokenid,
                                @Field("device") String device);

    @POST("Check_volunteer_schedule_status")
    @FormUrlEncoded
    Call<ScheduleStatusModel> ScheduleStatusModel(@Field("volunteer_id") String volunteer_id);

    @POST("volunteer_login/first_login")
    @FormUrlEncoded
    Call<MessageModel> firstLogin(@Field("volunteer_id") String volunteer_id);

    @POST("manager_on_duty")
    Call<ModModel> modAPI();

    @POST("volunteer_forgot_password")
    @FormUrlEncoded
    Call<MessageModel> volunteer_forgot_password(@Field("email") String email);

    @POST("volunteer_info")
    @FormUrlEncoded
    Call<List<ProfileInfoModel>> volunteer_info(@Field("volunteer_id") String volunteer_id);


    @POST("volunteer_info_update")
    @FormUrlEncoded
    Call<UpdateprofileModel> volunteer_info_update(
            @Field("volunteer_id") String volunteer_id,
            @Field("fullname") String fullname,
            @Field("mobile") String mobile,
            @Field("address") String address,
            @Field("whatsapp") String whatsapp,
            @Field("other_lang") String other_lang,
            @Field("dateofbirth") String dateofbirth);

    @Multipart
    @POST("Volunteer_pic_update")
    Call<ProfilePic> Volunteer_pic_update(@Part("volunteer_id") RequestBody volunteer_id,
                                          @Part MultipartBody.Part profile_pic);


    @POST("Volunteer_upcoming_shift")
    @FormUrlEncoded
    Call<List<UpcomingShift>> Volunteer_upcoming_shift(@Field("volunteer_id") String volunteer_id);


    @POST("Volunteer_upcoming_shift_list")
    @FormUrlEncoded
    Call<List<UpcomingShift>> Volunteer_upcoming_shift_list(@Field("volunteer_id") String volunteer_id);


    @POST("helpvictim/helpvictimlist")
    Call<HelpRequestModel> helpvictimlist();

    @POST("Admin_notes")
    Call<List<Announcement>> Admin_notes1();

    @POST("Admin_notes")
    Call<JsonArray> Admin_notes();


    @POST("Open_shifts_by_year")
    Call<List<OpenShift>> Open_shifts_by_year();

    @POST("Openshifts_by_month_list")
    @FormUrlEncoded
    Call<List<OpenShift>> Open_shifts_by_year(@Field("month") String month);


    @POST("Volunteer_schedule_by_year")
    @FormUrlEncoded
    Call<List<MyShift>> myShift(
            @Field("volunteer_id") String volunteer_id);

    @POST("Volunteer_schedule_by_month")
    @FormUrlEncoded
    Call<List<MyShift>> myShift(
            @Field("volunteer_id") String volunteer_id,
            @Field("month") String month);

    @POST("Shift_on_date")
    @FormUrlEncoded
    Call<List<OnDateShift>> Shift_on_date(
            @Field("date") String date);

    @POST("Volunteer_shift_accept")
    @FormUrlEncoded
    Call<MessageModel> shift_accept(
            @Field("date") String date,
            @Field("volunteer_id") String volunteer_id,
            @Field("schedule_id") String schedule_id);

    @POST("volunteer_shift_request/accept")
    @FormUrlEncoded
    Call<MessageModel> requwst_shift_accept(
            @Field("date") String date,
            @Field("volunteer_id") String volunteer_id,
            @Field("schedule_id") String schedule_id);

    @POST("Volunteer_shift_cancel")
    @FormUrlEncoded
    Call<MessageModel> shift_cancel(
            @Field("volunteer_id") String volunteer_id,
            @Field("schedule_id") String schedule_id);

    @POST("volunteer_shift_request")
    @FormUrlEncoded
    Call<MessageModel> shift_request(
            @Field("volunteer_id") String volunteer_id,
            @Field("schedule_id") String schedule_id,
            @Field("reason") String reason);


    @POST("volunteer_shift_request/list")
    @FormUrlEncoded
    Call<List<ShiftRequestMode>> request_list(
            @Field("volunteer_id") String volunteer_id);

    @POST("volunteer_shift_request/list")
    @FormUrlEncoded
    Call<MessageModel> request_list_message(
            @Field("volunteer_id") String volunteer_id);

    @POST("Upcoming_open_shift")
    @FormUrlEncoded
    Call<JsonObject> Upcoming_open_shift(
            @Field("date") String date);

    @POST("Upcoming_open_shift_wise")
    @FormUrlEncoded
    Call<JsonObject> Upcoming_open_shift_wise(
            @Field("date") String date,
            @Field("shift") String shift);

    @POST("Volunteer_cpassword")
    @FormUrlEncoded
    Call<MessageModel> Volunteer_cpassword(@Field("volunter_id") String volunter_id,
                                           @Field("password") String password);


    @POST("volunteer_info/availability")
    @FormUrlEncoded
    Call<MessageModel> volunteerAvailability(@Field("volunteer_id") String volunter_id,
                                           @Field("status") String status);

    @POST("victim/helpline")
    @FormUrlEncoded
    Call<CaseModel> victim_helpline(@Field("user_type") String user_type,
                                    @Field("connection_type") String connection_type,
                                    @Field("language") String language,
                                    @Field("device_id") String device_id,
                                    @Field("device_type") String device_type,
                                    @Field("fcm_token") String fcm_token,
                                    @Field("name") String name,
                                    @Field("crisis") String crisis,
                                    @Field("age") String age,
                                    @Field("gender") String gender,
                                    @Field("safe_to_call") String safe_to_call,
                                    @Field("mobile") String mobile,
                                    @Field("race") String race,
                                    @Field("about") String about,
                                    @Field("case_id") String case_id);

    @GET("accessToken.php")
    Call<ModelToken> token(@Query("identity") String identity);

    @POST("schedule/timer")
    @FormUrlEncoded
    Call<MessageModel> timer(@Field("volunter_id") String volunter_id);

    @POST("helpvictim/accept")
    @FormUrlEncoded
    Call<MessageModel> helpvictimAccept(@Field("id") String volunter_id);

    @GET("cases")
    Call<ChannelListModel> getChannelList();

    @POST("volunteer_info/check_in")
    @FormUrlEncoded
    Call<MessageModel> checkin(@Field("volunteer_id") String volunteerID,  @Field("status") String status);

    @POST("volunteer_info/check_out")
    @FormUrlEncoded
    Call<MessageModel> checkout(@Field("volunteer_id") String volunteerID,  @Field("status") String status);

    @GET("survivor_tools.php")
    Call<SurvivorModel> getSurvivorResults();

    @POST("defaultChatMod.php")
    @FormUrlEncoded
    Call<MessageModel> defaultMessages(@Field("channel") String channel,
                                       @Field("from") String sender,
                                       @Field("body") String body);

    @POST("Case_status_update")
    @FormUrlEncoded
    Call<ChannelStatusModel> postCaseStatus(@Field("case_id") String caseId, @Field("volunteer_id") String volenteerID, @Field("attended_id") String attended_id,
                                            @Field("victim_id") String victim_id, @Field("status") String status);


    @POST("helpvictim/languagelist")
    Call<List<AdditionalLanguageModel>> getAdditionalLanguage();

    @POST("helpvictim")
    @FormUrlEncoded
    Call<LanguageRequestModel> LanguageRequest(@Field("language") String language, @Field("case_id") String case_id, @Field("message") String message,@Field("support_type") String supportType);

}

