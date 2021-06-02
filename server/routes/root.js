var express = require('express');
var router = express.Router();

//@brief '/' : 함수가 적용되는 경로(라우트). -> 미들웨어 설정
router.get('/', (req, res) => res.send('Hello World!')) // @brief : res.send() : 응답 보내주기
module.exports = router;