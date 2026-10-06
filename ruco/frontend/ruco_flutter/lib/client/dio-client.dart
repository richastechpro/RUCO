import 'dart:convert';
import 'package:dio/dio.dart';
import 'package:localstorage/localstorage.dart';
 
class DioClient {
  static final Dio dio =
      Dio()
        ..interceptors.add(
          InterceptorsWrapper(
            onRequest: (options, handler) {
              final userJson = localStorage.getItem('user');
              if (userJson != null) {
                final user = jsonDecode(userJson);
                final token = user['token'];
                if (token != null) {
                  options.headers['Authorization'] = 'Bearer $token';
                }
              }
              return handler.next(options);
            },
          ),
        );
}
