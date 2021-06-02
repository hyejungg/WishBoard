var express = require('express')
var db = require('../db')
var router = express.Router(); // @brief : express.Router() : router 객체를 생성

// @brief '/new' : 함수가 적용되는 경로(라우트)
router.get('/new', (req, res) => res.send('item!'))

// @brief  item/new 안드로이드에서 작성한 경로
router.post('/new', function(req, res, next) {
    if(!req.body.item_name){ // @brief : 상품명 없으면 저장 안됨
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

/*
// @brief : pool 사용하기
const getConnection = require('../db_connection');

//var express = require('express');
var bodyParser = require('body-parser');
//var app = express();

app.use(bodyParser.json({extended: true})); //사용자가 웹사이트로 전달하는 정보>들을 검사하는 미들웨어
app.use(bodyParser.urlencoded({extended: true})); //json이 아닌 post형식으로올때
 파서

 router.get('/', function (req, res) {
    // @brief req.body.user_id : 안드로이드에서 user_id로 보낸 값 받아옴
    // @brief Number : 안드로이드에서 문자열로 보냈기 때문에 숫자로 형변환 필요.

    // 삽입을 수행하는 sql문.
    var sql = 'INSERT INTO items (user_id, folder_id, item_image, item_name, item_price, item_url, item_memo) VALUES (?, ?, ?, ?, ?, ?, ?)';
    var params = [
        1, 1,"none","TERRY SHORTS (3COLORS)",89000,"https://store.musinsa.com/app/goods/1916401","test"
    ];

 // @brief : 커넥션 풀에서 연결 객체를 가져옴
     getConnection((conn) => {
        conn.query(sql, params, function (err, result) {  // @brief : sql 문의 ?는 두번째 매개변수로 넘겨진 params의 값으로 치환
            var resultCode = 404;
            var message = '에러가 발생했습니다';

            if (err) {
                console.log(err);
                console.error('INSERT Error', error);
            } else {
                resultCode = 200;
                message = '아이템을 등록했습니다.';
                console.log('results: ', results); //@deprecate : 추후삭제 예정
            }

            res.json({'code': resultCode, 'message': message});
        });
        conn.release(); //@brief : 사용후 Pool에 Connection을 반환
    });
});

//@brief /item/newItem : 안드로이드에서 작성한 경로
router.post('/', function (req, res) {
    // @brief req.body.user_id : 안드로이드에서 user_id로 보낸 값 받아옴
    // @brief Number : 안드로이드에서 문자열로 보냈기 때문에 숫자로 형변환 필요.
    var user_id = Number(req.body.user_id);
    var folder_id = Number(req.body.folder_id);
    var item_image = req.body.item_image;
    var item_name = req.body.item_name;
    var item_price = Number(req.body.item_price);
    var item_url = req.body.item_url;
    var item_memo = req.body.item_memo;

    var sql = 'INSERT INTO items (user_id, folder_id, item_image, item_name, item_price, item_url, item_memo) VALUES (?, ?, ?, ?, ?, ?, ?)';
    // 삽입을 수행하는 sql문.
    var params = [
       user_id,
        folder_id,
        item_image,
        item_name,
        item_price,
        item_url,
        item_memo
    ];

    // @brief : 커넥션 풀에서 연결 객체를 가져옴
     getConnection((conn) => {
        conn.query(sql, params, function (err, result) {  // @brief : sql 문의 ?는 두번째 매개변수로 넘겨진 params의 값으로 치환된다.
            var resultCode = 404;
            var message = '에러가 발생했습니다';

            if (err) {
                console.log(err);
                console.error('INSERT Error', error);
            } else {
                resultCode = 200;
                message = '아이템을 등록했습니다.';
                console.log('results: ', results); //@deprecate : 추후삭제 예정
            }

            res.json({'code': resultCode, 'message': message});
        });
        conn.release(); //@brief : 사용후 Pool에 Connection을 반환
    });
});*/

