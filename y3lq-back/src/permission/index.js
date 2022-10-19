export default function hasPermission(params) {
    let tag = false;
    let perms = JSON.parse(sessionStorage.getItem("perms")); //循环遍历权限字段列表
    for (let i = 0; i < perms.length; i++) {
        if (perms[i] === params) {
            tag = true;
            break; 
        }
    }
    return tag;
}