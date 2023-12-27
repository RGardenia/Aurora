var fs = require('fs')

const dirIsExist = function(dirNamem,res){
    fs.readdir(dirNamem, function(err, files){
        if(err){
            res.code = 10000
            res.msg = '目录不存在！'
        }else{
            res.code = 20000
            res.msg = '成功！'
            res.data = files
        }
    })
}

// 导出
// exports.dirIsExist = dirIsExist;

module.exports = {
    dirIsExist
}