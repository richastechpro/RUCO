import 'dart:convert';

import 'package:flutter/material.dart';

class SnackBarHelper {
  static void show(
    BuildContext context,
    dynamic payload, {
    Duration duration = const Duration(seconds: 2),
  }) {
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(content: Text(jsonEncode(payload)), duration: duration),
    );
  }
}
