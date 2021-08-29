var express = require("express");
var db = require("../db");
var router = express.Router(); // @brief : express.Router() : router 객체를 생성

// @breif : fcm 알림 설정
const admin = require('firebase-admin');
let serAccount = require('../private/serviceAccountKey.json');
admin.initializeApp({
  credential: admin.credential.cert(serAccount)
});

// @brief : router 핸들링 함수(비동기)를 async함수로 리턴하는 랩핑함수
const wrapper = asyncFn => { return (async (req, res, next) => { try { return await asyncFn(req, res, next); } catch (error) { return next(error); } }); };

//@param ms : 밀리초 만큼 시간 지연
function sleep(ms) {
  return new Promise(resolve=>setTimeout(resolve, ms));
}

// @brief : 알림 등록
router.post("/", function(req, res){
    var user_id = Number(req.body.user_id);
    var item_id = Number(req.body.item_id);
    var item_notification_type = req.body.item_notification_type;
    var item_notification_date = req.body.item_notification_date;
    var token = req.body.token; // @param : fcm 토큰

   // @brief : FCM 데이터 페이로드
   var message = {
    data: {
      title: '상품일정알림',
      message: item_notification_type + ' 알림이 있습니다.',
      isScheduled : 'true',
      scheduledTime: item_notification_date
    },
    token: token
    };
    console.log(message);

    var sql_insert = "INSERT INTO notification (user_id, item_id, item_notification_type, item_notification_date) VALUES(?,?,?,?)";
    var params = [user_id, item_id, item_notification_type, item_notification_date];
    console.log("sql_insert : " + sql_insert);

    // @brief : DB에 알림정보 저장
    db.get().query(sql_insert, params, wrapper(async(err, result)=>{
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

                  await sleep(2000, message);
                // @brief : DB에 알림이 성공적으로 추가된 경우 fcm 알림 전송
                 admin.messaging().send(message)
                    .then(function (response) {
                      console.log('[fcm] Successfully sent message: : ' + response)
                    })
                    .catch(function (err) {
                      console.log('[fcm] Error Sending message!!! : ' + err)
                    });

                  res.status(200).json({
                    success: true,
                    message: "알림 데이터베이스 추가 성공",
                  });
                }
              }
         db.releaseConn();
    }));
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
router.put('/:item_id', function(req, res){
  var item_id = Number(req.params.item_id);
  console.log( "item_id : " + item_id);

  var sql = "UPDATE notification SET is_read = 1 WHERE item_id = ?";
  console.log("sql_update : " + sql);

  db.get().query(sql, [item_id], function (err, result) {
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

// @brief : 알림정보 수정하기
router.put('/detail/:item_id', function(req, res){
  var item_id = Number(req.params.item_id);
  var item_notification_type = req.body.item_notification_type;
  var item_notification_date = req.body.item_notification_date;

  console.log( "item_id : " + item_id);

  var sql = "UPDATE notification SET item_notification_type = ?, item_notification_date = ? WHERE item_id = ?";
  console.log("sql_update : " + sql);

  db.get().query(sql, [item_notification_type, item_notification_date, item_id], function (err, result) {
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

// @brief : 알림 삭제
router.delete('/detail/:item_id', function(req, res){
  var item_id = Number(req.params.item_id);
  console.log("item_id : " + item_id)

  var sql = "DELETE FROM notification WHERE item_id = ?";

  console.log("sql_delete : " + sql);

  db.get().query(sql, [item_id], function (err, result) {
    if (err) {
      console.log(err);
    } else {
      if (result.length === 0) {
        console.log("Failed to deleted the items for data.");
        res.status(500).json({
          success: false,
          message: "wish boarad 서버 에러",
        });
      } else {
        console.log("Successfully deleted data into the items!!");
        res.status(200).json({
          success: true,
          message: "알림 삭제 성공",
        });
      }
    }
    db.releaseConn();
  });
});

module.exports = router;
