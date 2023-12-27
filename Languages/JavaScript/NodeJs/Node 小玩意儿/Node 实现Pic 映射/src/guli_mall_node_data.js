var http = require('http')
var fs = require('fs')
var { result } = require('../utils/commonUtil')
var { dirIsExist } = require('../utils/fsUtil');
const { stringify } = require('querystring');

var port = 9998

var solveErr = function (res, result){
    res.setHeader('Content-Type', 'text/plain; charset=utf-8')
    result.msg = '文件读取失败, 请稍后重试！'
}

var server = http.createServer();
server.on('request', function(req, res){
    var typeUrl = req.url.substr(21, 3);
    var fileUrl = req.url.substring(25);
    if(typeUrl === 'img'){
        fs.readFile('../../data/img/' + fileUrl, "binary", function(err, data){
            if(err){
                solveErr(res, result)
                res.end(JSON.stringify(result))
            }else{
                res.writeHead(200, {'Content-Type': 'image/jpeg'});
                // 会直接下载图片
                // res.setHeader('Content-Type', 'application/x-img');
                res.write(data, "binary")
                res.end()
            }
        })
    }else if(typeUrl === 'mp4'){
        console.log(fileUrl)
        fs.readFile('../../data/mp4/' + fileUrl, "binary", function(err, data){
            if(!err){
                res.writeHead(200, {'Content-Type': 'audio/mp4'});
                res.write(data, "binary")
                res.end()
            }else{
                solveErr(res, result)
                res.end(JSON.stringify(result))
            }
        })
    }
    else {  // 处理 404 等状态异常
        fs.readFile('./views/error.html', function(err, data){
            if(err){
                res.end('404 NOT FOUND !')
            }else{
                res.setHeader('Content-Type', 'text/html; charset=utf-8');
                res.end(data);
            }
        })
    }
})

server.listen(port, function(){
    console.log('Server is listening...')
})