// import 'dart:io';
// import 'dart:typed_data';

// import 'package:flutter/material.dart';
// import 'package:path_provider/path_provider.dart';
// import 'package:permission_handler/permission_handler.dart';
// import 'package:screenshot/screenshot.dart';

// class CertificatePage extends StatefulWidget {
//   const CertificatePage({super.key});

//   @override
//   State<CertificatePage> createState() => _CertificatePageState();
// }

// class _CertificatePageState extends State<CertificatePage> {
//   final ScreenshotController screenshotController = ScreenshotController();

//   Future<void> captureAndSave() async {
//     final status = await Permission.storage.request();
//     if (!status.isGranted) {
//       ScaffoldMessenger.of(
//         context,
//       ).showSnackBar(SnackBar(content: Text('Storage permission denied')));
//       return;
//     }

//     final Uint8List? imageBytes = await screenshotController.capture();
//     if (imageBytes == null) return;

//     final directory = await getExternalStorageDirectory();
//     final path =
//         '${directory!.path}/certificate_${DateTime.now().millisecondsSinceEpoch}.png';
//     final file = File(path);
//     await file.writeAsBytes(imageBytes);

//     ScaffoldMessenger.of(
//       context,
//     ).showSnackBar(SnackBar(content: Text('Saved to $path')));
//   }

//   @override
//   Widget build(BuildContext context) {
//     return Scaffold(
//       appBar: AppBar(title: Text("Certificate")),
//       body: SingleChildScrollView(
//         child: Container(
//           width: 600,
//           height: 800,
//           decoration: BoxDecoration(
//             image: DecorationImage(
//               image: AssetImage("assets/bg-background_new.png"),
//               fit: BoxFit.cover,
//             ),
//           ),
//           child: Column(
//             children: [
//               Screenshot(
//                 controller: screenshotController,
//                 child: Stack(
//                   children: [
//                     Positioned(
//                       top: 10,
//                       left: 0,
//                       child: Image.asset("assets/rlogo.png", width: 120),
//                     ),
//                     Positioned(
//                       top: 10,
//                       right: 0,
//                       child: Image.asset(
//                         "assets/blue_stone_logo.png",
//                         width: 140,
//                       ),
//                     ),
//                     Column(
//                       children: [
//                         SizedBox(height: 80),
//                         Text(
//                           "CERTIFICATE OF DISCHARGE",
//                           textAlign: TextAlign.center,
//                           style: TextStyle(
//                             fontSize: 22,
//                             fontWeight: FontWeight.bold,
//                           ),
//                         ),
//                         SizedBox(height: 16),
//                         Text(
//                           "With reference to the notification for Food Safety and Standards (Licensing and Registration of food business) First Amendment Regulation2017, w.r.t. quality of vegetable oil for repeated frying in the official gazette of India S.O. No. 2492 (E) and FSSAI notification 11/fssai-2018 dated 9th August 2019.\n\nIt may be noted that M/s. BLUE STONE ENERGY PVT LTD has collected the Used Cooking Oil from Devyani International LTD- Huda Metro Station-K549 as per details mentioned below -",
//                           style: TextStyle(fontSize: 15),
//                           textAlign: TextAlign.justify,
//                         ),
//                         SizedBox(height: 20),
//                         Stack(
//                           alignment: Alignment.center,
//                           children: [
//                             Opacity(
//                               opacity: 0.1,
//                               child: Image.asset(
//                                 "assets/bg-logo_new.png",
//                                 width: 300,
//                               ),
//                             ),
//                             Table(
//                               border: TableBorder.all(color: Colors.black),
//                               columnWidths: {
//                                 0: FixedColumnWidth(60),
//                                 1: FixedColumnWidth(100),
//                                 2: FixedColumnWidth(150),
//                                 3: FixedColumnWidth(100),
//                               },
//                               children: [
//                                 TableRow(
//                                   decoration: BoxDecoration(
//                                     color: Colors.grey[300],
//                                   ),
//                                   children: [
//                                     _tableCell("Sr.No."),
//                                     _tableCell("Date"),
//                                     _tableCell("GST Invoice Number"),
//                                     _tableCell("Quantity Kgs"),
//                                   ],
//                                 ),
//                                 TableRow(
//                                   children: [
//                                     _tableCell("1"),
//                                     _tableCell("03-06-2025"),
//                                     _tableCell("1231"),
//                                     _tableCell("150"),
//                                   ],
//                                 ),
//                               ],
//                             ),
//                           ],
//                         ),
//                         SizedBox(height: 30),
//                         Row(
//                           mainAxisAlignment: MainAxisAlignment.spaceBetween,
//                           children: [
//                             Image.asset(
//                               "assets/biodiesel-cert.png",
//                               width: 100,
//                             ),
//                             Column(
//                               children: [
//                                 Image.asset(
//                                   "assets/signature-cert.png",
//                                   width: 120,
//                                 ),
//                                 SizedBox(height: 5),
//                                 Text("Srivats Chaturvedi"),
//                                 Text("Director"),
//                               ],
//                             ),
//                           ],
//                         ),
//                         SizedBox(height: 20),
//                       ],
//                     ),
//                   ],
//                 ),
//               ),
//               SizedBox(height: 20),
//               ElevatedButton.icon(
//                 icon: Icon(Icons.download),
//                 label: Text("Save Certificate"),
//                 onPressed: captureAndSave,
//               ),
//             ],
//           ),
//         ),
//       ),
//     );
//   }

//   Widget _tableCell(String text) => Padding(
//     padding: const EdgeInsets.all(8.0),
//     child: Text(text, textAlign: TextAlign.center),
//   );
// }


import 'package:flutter/material.dart';

class CertificatePage extends StatefulWidget {
  const CertificatePage({super.key});

  @override
  State<CertificatePage> createState() => _CertificatePageState();
}

class _CertificatePageState extends State<CertificatePage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold();
  }
}