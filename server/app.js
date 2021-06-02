// @brief 1.Express app을 생성
var express = require('express')
var app = express()
var bodyParser = require('body-parser'); // @brief bodyParse : 요청의 본문에 있는 데이터를 해석해서 req.body 객체로 만들어주는 미들웨어
var db = require('./db'); // @brief : db pool 사용하기

// @brief 2.라우트할 모듈
var root_router = require('./routes/root');
var item = require('./routes/item');

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: false}));

/*
 * @ brief 3.별도 파일에서 라우트 함수를 작성할 때는 express.Router() 함수를통해 호출
 * module.exports = router 을 해주면 해당 파일에서 선언한 함수를 router를 통해서 사용할 수 있다.
 */
app.use('/', root_router);
app.use('/item', item); //@ brief /new_item : 안드로이드에서 작성한 경로

//@brief 4.데이터베이스 연결
 db.connect(function(err){
    if(err){
      console.log('데이터베이스에 접속할 수 없습니다');
      process.exit(1);
    }
  });

  // @brief catch 404 and forward to error handler
app.use(function(req, res, next) {
    var err = new Error('Not Found');
    err.status = 404;
    next(err);
  });

// @brief error handler
app.use(function(err, req, res, next) {
    // @brief set locals, only providing error in development
    res.locals.message = err.message;
    res.locals.error = req.app.get('env') === 'development' ? err : {};

    // @brief render the error page
    res.status(err.status || 500);
    res.render('error');
  });

module.exports = app;


/*
// @ brief : 데이터베이스 연결
var mysql      = require('mysql');
var connection = mysql.createConnection({
  host     : '',
  user     : '',
  password : '',
  database : ''
});

// 삽입을 수행하는 sql문.
var sql = 'INSERT INTO items (user_id, folder_id, item_image, item_name, item_price, item_url, item_memo) VALUES (?, ?, ?, ?, ?, ?, ?)';
var params = [
    user_id,
    folder_id,
    item_image,
    item_name,
    item_price,
    item_url,
    item_memo
];

connection.connect();

connection.query(sql, params, function (err, result) {  // @brief : sql 문의 ?는 두번째 매개변수로 넘겨진 params의 값으로 치환된다.
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

connection.end();


app.get('/item/newItem', function (req, res) {
    // @brief req.body.user_id : 안드로이드에서 user_id로 보낸 값 받아옴
    // @brief Number : 안드로이드에서 문자열로 보냈기 때문에 숫자로 형변환 필요.

    // 삽입을 수행하는 sql문.
    var sql = 'INSERT INTO items (user_id, folder_id, item_image, item_name, item_price, item_url, item_memo) VALUES (?, ?, ?, ?, ?, ?, ?)';
    var params = [
        1, 1,
        "none",
        "TERRY SHORTS (3COLORS)",
        89000,
        "https://store.musinsa.com/app/goods/1916401",
        "youngjin"
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
});

//@brief /item/newItem : 안드로이드에서 작성한 경로
app.post('/item/newItem', function (req, res) {
    // @brief req.body.user_id : 안드로이드에서 user_id로 보낸 값 받아옴
    // @brief Number : 안드로이드에서 문자열로 보냈기 때문에 숫자로 형변환 필요.
    var user_id = Number(req.body.user_id);
    var folder_id = Number(req.body.folder_id);
    var item_image = req.body.item_image;
    var item_name = req.body.item_name;
    var item_price = Number(req.body.item_price);
    var item_url = req.body.item_url;
    var item_memo = req.body.item_memo;

    // 삽입을 수행하는 sql문.
    var sql = 'INSERT INTO items (user_id, folder_id, item_image, item_name, item_price, item_url, item_memo) VALUES (?, ?, ?, ?, ?, ?, ?)';
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
});
*/

/*
 * @see https://youngest-programming.tistory.com/123
        https://github.com/qskeksq/AndroidOpen_BestFood
*/