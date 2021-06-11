var express = require("express");
var db = require("../db");
var router = express.Router(); // @brief : express.Router() : router 객체를 생성

// @brief : 장바구니 조회
router.get("/:user_id", function (req, res) {
  var user_id = Number(req.params.user_id);

  var sql =
  "SELECT a.item_image, a.item_name, a.item_price, b.item_count FROM items a JOIN basket b ON a.item_id = b.item_id WHERE b.user_id = ?";

  console.log("sql_select : " + sql);

  db.get().query(sql, [user_id], function (err, result) {
    if (err) {
      console.log(err);
    } else {
      if (result.length === 0) {
        console.log("Failed to selected the basket for data.");
        res.status(500).json({
          success: false,
          message: "wish boarad 서버 에러",
        });
      } else {
        console.log("Successfully selected data the basket!!");
	res.status(200).json(result);
/*        res.status(200).json({
          success: true,
          message: "장바구니 데이터베이스 접근 성공",
	 // item: result,
  /*        item_image: result[0].item_image,
          item_name: result[0].item_name,
          item_price: result[0].item_price,
          item_count: result[0].item_count, */
 //     });
	      console.log(result);
      }
      db.releaseConn();
    }
  });
});

// @brief : 장바구니 추가
router.post("/", function (req, res) {
  var user_id = Number(req.body.user_id);
  var item_id = Number(req.body.item_id);

  var sql = "INSERT INTO basket (user_id, item_id) VALUES (?, ?)";
  var params = [user_id, item_id];

  console.log("sql_insert : " + sql);

  db.get().query(sql, params, function (err, result) {
    if (err) {
      console.log(err);
    } else {
      if (result.length === 0) {
        console.log("Failed to inserted the basket for data.");
        res.status(500).json({
          success: false,
          message: "wish boarad 서버 에러",
        });
      } else {
        console.log("Successfully inserted data into the basket!!");
        res.status(200).json({
          success: true,
          message: "장바구니 데이터베이스 추가 성공",
        });
      }
      db.releaseConn();
    }
  });
});

// @brief : 장바구니 삭제
router.delete("/:user_id/:item_id", function (req, res) {
  var user_id = Number(req.body.user_id);
  var item_id = Number(req.body.item_id);

  var sql = "DELETE FROM basket WHERE user_id = ? AND item_id = ?";
  var params = [user_id, item_id];

  console.log("sql_delete : " + sql);

  db.get().query(sql, params, function (err, result) {
    if (err) {
      console.log(err);
    } else {
      if (result.length === 0) {
        console.log("Failed to deleted the basket for data.");
        res.status(500).json({
          success: false,
          message: "wish boarad 서버 에러",
        });
      } else {
        console.log("Successfully deleted data into the basket!!");
        res.status(200).json({
          success: true,
          message: "장바구니 데이터베이스 삭제 성공",
        });
      }
      db.releaseConn();
    }
  });
});

module.exports = router;

