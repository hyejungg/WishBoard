var express = require("express");
var db = require("../db");
var router = express.Router(); // @brief : express.Router() : router 객체를 생성

// @brief '/' : 함수가 적용되는 경로(라우트)
// /noti
router.post("/", function(req, res) {
    var user_id = Number(req.body.user_id);
    var item_id = Number(req.body.item_id);
    var item_notification_type = req.body.item_notification_type;
    var item_notification_date = req.body.item_notification_date;
    var sql_insert = "INSERT INTO notification (user_id, item_id, item_notification_type, item_notification_date) VALUES(?,?,?,?)";

    var params = [user_id, item_id, item_notification_type, item_notification_date];

    console.log("sql_insert : " + sql_insert);

    db.get().query(sql_insert, params, function(err, result){
        if(err) {
            console.log(err);
        } else {
              if (result.length === 0) {
                  console.log("Failed to inserted the notification for data.");
                  res.status(500).json({
                    success: false,
                    message: "wish boarad 서버 에러",
                  });
                } else {
                  console.log("Successfully inserted data into the notification!!");
                  res.status(200).json({
                    success: true,
                    message: "알림 데이터베이스 추가 성공",
                  });
                }
                db.releaseConn();
              }
    });
});

module.exports = router;
