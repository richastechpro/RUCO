import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:localstorage/localstorage.dart';

class UserAvatar extends StatefulWidget {
  const UserAvatar({super.key});

  @override
  State<UserAvatar> createState() => _UserAvatarState();
}

class _UserAvatarState extends State<UserAvatar> {
  String username = '';
  @override
  void initState() {
    super.initState();
    loadUserData();
  }

  void loadUserData() {
    final user = localStorage.getItem('user');
    print('user: $user');
    final userJson = jsonDecode(user!);
    print('userJson: ${userJson['username']}');
    setState(() {
      username = userJson['username'];
    });
  }

  @override
  Widget build(BuildContext context) {
    return Column(
      mainAxisAlignment: MainAxisAlignment.center,
      crossAxisAlignment: CrossAxisAlignment.center,
      children: [
        CircleAvatar(
          radius: 40,
          backgroundColor: Colors.white,
          backgroundImage: AssetImage('assets/blue-circle-with-white.avif'),
        ),
        const SizedBox(height: 10),
        Text(
          username,
          style: TextStyle(
            color: Colors.white,
            fontSize: 20,
            fontWeight: FontWeight.bold,
          ),
        ),
      ],
    );
  }
}
