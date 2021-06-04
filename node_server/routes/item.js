var express = require('express')
var db = require('../db')
var router = express.Router(); // @brief : express.Router() : router 객체를 생성

// @brief '/' : 함수가 적용되는 경로(라우트)
router.get('/new', (req, res) => res.send('item!'))

// food/info
router.post('/new', function(req, res, next) {
    if(!req.body.item_image){
        return res.status(400).send("정보가 없습니다");
    }

    var user_id = Number(req.body.user_id);
    var folder_id = Number(req.body.folder_id);
    var item_image = req.body.item_image;
    var item_name = req.body.item_name;
    var item_price = Number(req.body.item_price);
    var item_url = req.body.item_url;
    var item_memo = req.body.item_memo;

    var sql_insert = 
    "insert into items (user_id, folder_id, item_image, item_name, item_price, item_url, item_memo)"
    + " values(?,?,?,?,?,?,?);";

    var params = [
        user_id,
         folder_id,
         item_image,
         item_name,
         item_price,
         item_url,
         item_memo
     ];

    console.log("sql_insert : "+ sql_insert);

    db.get().query(sql_insert, params, function(err, result){
        if(err){
            console.log(err);
        }
        response.status(200).send(''+result.insertId); // @brief insertId : INSERT 문이 실행됐을 때, 삽입된 데이터의 id를 얻음, 폴더 ID 받아올 때 참고하기
    });
});

module.exports = router;
/* @brief : pool 사용하i기
const getConnection = require('../db_connection');
*/

