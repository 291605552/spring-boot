//设置全局表单提交格式
Vue.http.options.emulateJSON = true;

// Vue实例
new Vue({
    el: '#app',
    data() {
        //自定义用户校验
        var validateUser =(rule,value,callback) => {
                    var regUserName=/^[a-zA-Z0-9_]{6,12}$/;
                    if(value === ''){
                      callback(new Error('请输入用户名'));
                    } else if(regUserName.test(value)==false){
                      callback(new Error('用户名不合法'));
                    } else {
                      var param1= { username : value};
                      console.log(value);
                      axios.get('/verifyUserName',{params:param1}).then(function(result){
                      console.log(result);
                        if(result.data.success){
                             callback(result.data.message);
                        }else{
                             console.log(result.data.message);
                             callback();
                        }
                      }).catch(function(){
                             callback(new Error('服务异常'));

                      })
                    }

        };
        //自定义密码验证规则
        var validatePass = (rule, value, callback) => {
                    var regPd=/^\w{6,12}$/;
                    if (value === '') {
                      callback(new Error('请输入密码'));
                    } else if (regPd.test(value)==false){
                      callback(new Error('密码格式有误'));
                    } else {
                      if (this.register.repassword !== '') {
                        this.$refs.register.validateField('repassword');
                      }
                      callback();
                    }
        };
        var validatePass2 = (rule, value, callback) => {
                    if (value === '') {
                      callback(new Error('请再次输入密码'));
                    } else if (value !== this.register.password) {
                      callback(new Error('两次输入密码不一致!'));
                    } else {
                      callback();
                    }
        };
        //邮箱校验
        var validateEmail =(rule,value,callback) =>{
                    var regEmail=/^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
                    if (value === ''){
                      callback(new Error('请输入邮箱地址'));
                    } else if (regEmail.test(value)==false){
                      callback(new Error('邮箱格式有误'));
                    } else {
                      callback();
                    }
        };
        return {
            timeout:120,
            checked: false,
            register: {
                username: '',
                password: '',
                repassword: '',
                email:'',
                code:''
            },
            rules:{
                username:[
                     {
                        validator: validateUser,
                        trigger: 'blur'
                     }
                ],
               password:[{
                       validator: validatePass,
                       trigger: 'blur'
               }],
               repassword:[{
                       validator: validatePass2,
                       trigger: 'blur'
               }],
               email:[{
                       validator: validateEmail,
                       trigger: ['blur', 'change']
               }]
            }
        }

    },
    methods: {
        submitForm(register) {
              //提交表单,验证表单
              this.$refs[register].validate((valid)=>{
                 if(valid){
                      //验证无误，提交表单
                      this.$http.post("/register",{
                                  username: this.register.username,
                                  password: this.register.password,
                                  email:this.register.email,
                                  code:this.register.code
                      }).then(result=>{
                          console.log(result);
                          //// 判断用户是否登录成功，后端返回JSON格式数据，不然取不到数据
                          if (result.body.success) {
                               window.location.href = "/index";
                          } else {
                               // 弹出错误信息框
                               this.$emit(
                                   'submit-form',
                                   this.$message({
                                       message: result.body.message,
                                       type: 'warning',
                                       duration: 6000
                                   }),
                               );
                               // 清空表单状态
                               //this.$refs[register].resetFields();
                          }
                        });
                 }else{
                     this.$emit(
                          'submit-form',
                          this.$message({
                              message: '输入信息有误！',
                              type: 'warning',
                              duration: 6000
                          }),
                     );
                     return false;
                 }
              });
        },
        sendCode(register){
              //校验邮箱
              var regEmail=/^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
              if(regEmail.test(this.register.email)==false){
                 //邮箱格式不正确
                 // 弹出错误信息框
                 this.$emit(
                     'submit-form',
                     this.$message({
                         message: '邮箱格式不正确',
                         type: 'warning',
                         duration: 6000
                     }),
                 );
                 // 清空表单状态
                 this.$refs[register].resetFields();
              }else{
                 console.log(this.register.email);
                 axios.post("/sendCode",{email:this.register.email})
                 .then(function(result){
                     if(result.data.success){
                         /*this.$emit(
                             'submit-form',
                             this.$message({
                                message: '验证码已经成功发送',
                                type: 'success',
                                duration: 5000
                             }),
                         );*/
                         alert("验证码已经成功发送");
                     }else{
                         /*this.$emit(
                             'submit-form',
                             this.$message({
                                message: '今日发送次数已达上限',
                                type: 'warning',
                                duration: 5000
                             }),
                         );*/
                         alert("今日发送次数已达上限");
                         // 清空表单状态
                         this.$refs[register].resetFields();
                     }
                 })
              }
        },
        registerEnter(register){
            this.submitForm(register);
        }
    }
});
