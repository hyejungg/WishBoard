// @ brief : 데이터베이스 연결
const mysql = require('mysql');
var pool;
//const config = require('./db_config.json');
//let pool = mysql.createPool(config);

/**
 * @brief: db.js 파일로 따로 데이터베이스 관리를 해줌
 * 쿼리 할때마다 연결하지 않고 서버 연결시 미리 풀을 생성
 */
exports.connect = function(done) {
  pool = mysql.createPool({
      connectionLimit: 100,
      host     : '',
      user     : '',
      password : '',
      database : ''
  });
}

exports.get = function() {
return pool;
}

/*
function getConnection(callback) {
  pool.getConnection(function (err, conn) {
    if(!err) { // @brief : 연결 성공
      callback(conn);
    }
  });
}
module.exports = getConnection;
*/

// @see : http://techbless.me/2020/01/17/Node-js%EC%97%90%EC%84%9C-Mysql-Connection-Pool-%EC%9D%B4%EC%9A%A9%ED%95%98%EA%B8%B0/