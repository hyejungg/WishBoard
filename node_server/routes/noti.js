var express = require("express");
var db = require("../db");
var router = express.Router(); // @brief : express.Router() : router 객체를 생성

// @brief : 알림 등록
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

// @brief : 알림 조회
router.get('/:user_id', function(req, res, next) {
    var user_id = req.params.user_id;
    console.log("user_id : " + user_id);
    
    var sql = "SELECT i.item_id, i.item_image, item_name, item_notification_type, CAST(item_notification_date AS CHAR(19)) item_notification_date, is_read FROM notification n JOIN items i ON n.item_id = i.item_id WHERE n.user_id = ? and item_notification_date <= NOW() ORDER BY item_notification_date DESC";

    console.log("sql : " + sql);

    db.get().query(sql, [user_id], function (err, rows) {
          if (err) {
               console.log(err);
               res.sendStatus(400);
             } else {
               if (rows.length === 0) {
                 console.log("Failed to select notification data.");
                 res.status(500).json({
                   success: false,
                   message: "wish boarad 서버 에러",
                 });
               } else {
                 console.log("rows : " + JSON.stringify(rows));
		 res.status(200).json(rows);
               }
             }
         db.releaseConn();
    });
});

// @brief : 사용자가 조회한 알림은 읽은 알림으로 처리하기
router.put('/:user_id', function(req, res){
  var user_id = Number(req.params.user_id);
  console.log("user_id : " + user_id)

  var sql = "UPDATE notification SET is_read = 1 WHERE user_id = ? AND is_read = 0";
  console.log("sql_update : " + sql);

  db.get().query(sql, [user_id], function (err, result) {
    if (err) {
      console.log(err);
    } else {
      if (result.length === 0) {
        console.log("Failed to updated the notification for data.");
        res.status(500).json({
          success: false,
          message: "wish boarad 서버 에러",
        });
      } else {
        console.log("Successfully updated data into the notification!!");
        res.status(200).json({
          success: true,
          message: "알림 수정 성공",
        });
      }
    }
    db.releaseConn();
  });
});
module.exports = router;

