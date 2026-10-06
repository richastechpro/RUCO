import 'package:flutter/material.dart';

class Snackbar extends StatefulWidget {
  final String message;
  final Duration duration;

  const Snackbar({
    super.key,
    required this.message,
    this.duration = const Duration(seconds: 2),
  });

  @override
  State<Snackbar> createState() => _SnackbarState();
}

class _SnackbarState extends State<Snackbar> {
  @override
  void initState() {
    super.initState();

    WidgetsBinding.instance.addPostFrameCallback((_) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text(widget.message), duration: widget.duration),
      );
    });
  }

  @override
  Widget build(BuildContext context) {
    return const SizedBox.shrink();
  }
}
