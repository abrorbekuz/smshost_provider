SEND YOUR SMS MESSAGE FROM YOUR DEVICE/SIM CARD USING API
THIS IS PROVIDER APP FOR SMS_HOST SERVER CURRENTLY RUNNING ON https://sms.qodirov.uz (temporarily)

USAGE WILL BE PROVIDED THROUGH YOUTUBE VIDEO LATER

### Requirements:
- Android Version 6.0 or higher
- Android-Device which is able to send SMS

### Create an account
1. Go to https://sms.qodirov.uz/ and press login
![index](https://github.com/abrorbekuz/smshost_provider/blob/main/images/index.png?raw=true)

2. Jump to register page so you can register yourself as user
![login](https://github.com/abrorbekuz/smshost_provider/blob/main/images/login.png?raw=true)
3. Fill the form with your creds, and create account
![register](https://github.com/abrorbekuz/smshost_provider/blob/main/images/register.png?raw=true)
4. Go to your mail box to verify yourself. Note! If you are not verified you cannot access the dashboard !
And go to verify link
![email](https://github.com/abrorbekuz/smshost_provider/blob/main/images/email_confirm.png?raw=true)
![register](https://github.com/abrorbekuz/smshost_provider/blob/main/images/email_confirm_after.png?raw=true)
5. Download the Provier from https://github.com/abrorbekuz/smshost_provider/releases page
6. Launch the app on your device and log in
7. Get your device id from server or just your device

### Android-Limit:
Android´s default SMS-Limit are 30 SMS to a single phonenumber within 30 minutes.  
You can change your SMS-Limit for your device (root-permission is **not** required).
#### How to change Android-Limit:
1. Make sure you have enabled USB-Debugging on your device and you are ready to use ADB.
2. Connect your device to the pc and open the terminal.
3. Open the adb-shell via the command: `adb shell`
4. Change the value of the SMS-Limit to the number of SMS you want to send within the 30 minutes timeframe. Via the command:  
`settings put global sms_outgoing_check_max_count 100`   
This command allows you to send 100 SMS to a phonenumber within the 30 minutes timeframe.
5. If you want to also change the timeframe, you can use the command:  
`settings put global sms_outgoing_check_interval_ms 900000`  
This command reduces the timeframe to 15 minutes.  
If you entered both commands, you would be able to send 100 SMS to a phonenumber within 15 minutes.
### API-Usage
The default server-url is `http://sms.qodirov.uz/`.  
To send a SMS you can point to your address and use the /messages (eg. `http://sms.qodirov.uz/api/v1/mesaages/`).  
You have to send `to_phone_number` and `content`.

#### Example Curl (x-www-form-urlencoded)
content = "TEST API Abror"
to_phone_number = "+998914940152"

```shell
curl -X POST http://sms.qodirov.uz/api/v1/messages/ -H 'Cache-Control: no-cache' -H 'Content-Type: application/x-www-form-urlencoded' -H "Authorization: Bearer {your_token_from_server}" -d 'device=0b75776a-3721-44e3-9d33-2e43b4440cbc&to_phone_number=%2B998903900501&content="TEST, API Abror"'
```

![request](https://github.com/abrorbekuz/smshost_provider/blob/main/images/request.png?raw=true)

