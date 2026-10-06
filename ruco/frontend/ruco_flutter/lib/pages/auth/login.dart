import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:localstorage/localstorage.dart';
import 'package:ruco_flutter/client/dio-client.dart';
import 'package:ruco_flutter/constant/environment.dart';
import 'package:logger/logger.dart';
import 'package:ruco_flutter/constant/routes.dart';
import 'package:ruco_flutter/pages/utils/show_snackbar.dart';

class Login extends StatefulWidget {
  const Login({super.key});

  @override
  State<Login> createState() => _LoginState();
}

class _LoginState extends State<Login> {
  final emailController = TextEditingController(text: "pickupboyTN");
  final passwordController = TextEditingController(text: "1");
  final dio = DioClient.dio;
  final logger = Logger();
  bool isLoading = false;

  @override
  void dispose() {
    emailController.dispose();
    passwordController.dispose();
    super.dispose();
  }

  void login(String email, String password) async {
    setState(() => isLoading = true);

    try {
      var payload = {"email": email, "password": password};
      final String url = '${prod ? prodUrl : devUrl}/auth/login';
      // SnackBarHelper.show(context, url);
      final response = await dio.post(url, data: payload);
      if (response.statusCode == 200) {
        logger.i("Login successful: ${response.data}");
        localStorage.setItem('user', jsonEncode(response.data));
        setState(() => isLoading = false);
        if (mounted) {
          SnackBarHelper.show(context, 'Successfully logged in!');
        }
        if (mounted) await Navigator.pushNamed(context, Routes.collectionList);
      } else {
        logger.e(
          "Login failed: ${response.statusCode} ${response.statusMessage}",
        );
        // if (mounted) {
        //   SnackBarHelper.show(context, {
        //     'code': response.statusCode,
        //     'msg': response.statusMessage,
        //   });
        // }
        setState(() => isLoading = false);
      }
    } catch (e) {
      logger.e("Error during login: $e");
    } finally {
      setState(() => isLoading = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Stack(
        fit: StackFit.expand,
        children: [
          Image.asset('assets/background_new.png', fit: BoxFit.cover),
          Column(
            crossAxisAlignment: CrossAxisAlignment.center,
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Image.asset('assets/blue_stone_logo.png', height: 120),
              const SizedBox(height: 50),
              Padding(
                padding: const EdgeInsets.symmetric(horizontal: 24.0),
                child: Padding(
                  padding: const EdgeInsets.all(24),
                  child: Column(
                    children: [
                      TextField(
                        controller: emailController,
                        decoration: InputDecoration(
                          labelText: "Email",
                          labelStyle: const TextStyle(
                            fontWeight: FontWeight.bold,
                            letterSpacing: 1.2,
                          ),
                          enabledBorder: OutlineInputBorder(
                            borderRadius: BorderRadius.circular(5),
                            borderSide: const BorderSide(
                              color: Color(0xFFCCCCCC),
                              width: 2,
                            ),
                          ),
                          focusedBorder: OutlineInputBorder(
                            borderRadius: BorderRadius.circular(5),
                            borderSide: const BorderSide(
                              color: Color(0xFFCCCCCC),
                              width: 2,
                            ),
                          ),
                        ),
                      ),
                      const SizedBox(height: 20),
                      TextField(
                        controller: passwordController,
                        obscureText: true,
                        decoration: InputDecoration(
                          labelText: "Password",
                          labelStyle: const TextStyle(
                            fontWeight: FontWeight.bold,
                            letterSpacing: 1.2,
                          ),
                          enabledBorder: OutlineInputBorder(
                            borderRadius: BorderRadius.circular(8),
                            borderSide: const BorderSide(
                              color: Color(0xFFCCCCCC),
                              width: 2,
                            ),
                          ),
                          focusedBorder: OutlineInputBorder(
                            borderRadius: BorderRadius.circular(8),
                            borderSide: const BorderSide(
                              color: Color(0xFFCCCCCC),
                              width: 2,
                            ),
                          ),
                        ),
                      ),
                      const SizedBox(height: 30),
                      SizedBox(
                        width: double.infinity,
                        height: 50,
                        child: ElevatedButton(
                          style: ElevatedButton.styleFrom(
                            backgroundColor: Colors.indigoAccent,
                            shape: RoundedRectangleBorder(
                              borderRadius: BorderRadius.circular(32),
                            ),
                          ),
                          onPressed:
                              isLoading
                                  ? null
                                  : () {
                                    String email = emailController.text.trim();
                                    String password =
                                        passwordController.text.trim();
                                    login('${email}admin', password);
                                  },
                          child: const Text(
                            'Login',
                            style: TextStyle(
                              fontWeight: FontWeight.bold,
                              fontSize: 16,
                              letterSpacing: 1.2,
                              color: Colors.white,
                            ),
                          ),
                        ),
                      ),
                    ],
                  ),
                ),
              ),
              const SizedBox(height: 200),
            ],
          ),

          if (isLoading)
            const ModalBarrier(dismissible: false, color: Colors.black12),

          if (isLoading)
            const Center(
              child: CircularProgressIndicator(color: Colors.blueAccent),
            ),
        ],
      ),
    );
  }
}
