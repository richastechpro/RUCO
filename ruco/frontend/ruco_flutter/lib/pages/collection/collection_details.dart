import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import 'package:logger/web.dart';
import 'package:ruco_flutter/constant/environment.dart';

import '../../client/dio-client.dart' show DioClient;

class CollectionDetailPage extends StatefulWidget {
  const CollectionDetailPage({super.key});

  @override
  State<CollectionDetailPage> createState() => _CollectionDetailPageState();
}

class _CollectionDetailPageState extends State<CollectionDetailPage> {
  bool _isInitialized = false;
  DateTime? selectedDate;
  late final int id;
  final logger = Logger();
  bool isLoading = false;
  bool onStart = true;
  dynamic data = {};
  bool otpSend = false;
  String otpMsg = 'OTP Not Verified';
  final ImagePicker _picker = ImagePicker();
  final List<XFile> _capturedImages = [];
  String photoTxt = "No Photos Uploaded";

  final TextEditingController qtyController = TextEditingController();
  final TextEditingController dateController = TextEditingController();
  final TextEditingController invoiceController = TextEditingController();
  final TextEditingController remarkController = TextEditingController();
  final TextEditingController mobileNoController = TextEditingController();
  final TextEditingController otpController = TextEditingController();

  @override
  void dispose() {
    qtyController.dispose();
    dateController.dispose();
    invoiceController.dispose();
    remarkController.dispose();
    mobileNoController.dispose();
    otpController.dispose();
    _clearPhotos();
    super.dispose();
  }

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    if (_isInitialized) return;
    final data = ModalRoute.of(context)?.settings.arguments;
    if (data != null && data is Map<String, dynamic>) {
      final dynamic parsedId = data['id'];
      if (parsedId is int) {
        id = parsedId;
      } else if (parsedId is String) {
        id = int.tryParse(parsedId) ?? -1;
      }
      print('id: $id');
      _fetchData(id);
      _isInitialized = true;
    }
  }

  Future<void> _fetchData(int id) async {
    setState(() => isLoading = true);

    try {
      final String url = prod ? prodUrl : devUrl;
      final res = await DioClient.dio.get('$url/collection/$id');
      final dynamic fetchedData = res.data;
      print('data: $fetchedData');
      setState(() {
        isLoading = false;
        data = fetchedData;
        onStart = false;
      });
    } catch (e) {
      logger.e('Error fetching data: $e');
      setState(() {
        isLoading = false;
        onStart = false;
      });
    }
  }

  Future<void> _requestOTP() async {
    setState(() => isLoading = true);
    try {
      final String url = prod ? prodUrl : devUrl;
      final res = await DioClient.dio.get('$url/collection/send-otp');
      final dynamic fetchedData = res.data;
      print('otp: $fetchedData');
      setState(() {
        isLoading = false;
        otpSend = true;
      });
    } catch (e) {
      logger.e('Error fetching data: $e');
      setState(() {
        isLoading = false;
      });
    }
  }

  Future<void> _verifyOTP() async {
    setState(() => isLoading = true);
    try {
      final String url = prod ? prodUrl : devUrl;
      final res = await DioClient.dio.get(
        '$url/collection/verify-otp',
        queryParameters: {'otp': otpController.text},
      );
      final dynamic fetchedData = res.data;
      print('otp: $fetchedData');
      setState(() {
        isLoading = false;
        otpSend = true;
        otpMsg = 'OTP Verified';
      });
    } catch (e) {
      logger.e('Error fetching data: $e');
      setState(() {
        isLoading = false;
      });
    }
  }

  Future<void> _takePhoto() async {
    print('photo taking');
    final XFile? photo = await _picker.pickImage(source: ImageSource.camera);
    print('photo taken: $photo');

    if (photo != null) {
      setState(() {
        _capturedImages.add(photo);
        photoTxt = 'Photo captured: ${_capturedImages.length}';
      });
    }
  }

  void _clearPhotos() {
    // setState(() {
    //   _capturedImages.clear();
    // });
  }

  Future<void> _submit() async {
    try {
      final payload = {
        'qty': double.tryParse(qtyController.text),
        'date': dateController.text,
        'invoiceNo': invoiceController.text,
        'descr': remarkController.text,
        'collectionId': id,
        'type': 'SINGLE',
      };
      final String url = prod ? prodUrl : devUrl;
      final res = await DioClient.dio.put(
        '$url/collection/collect',
        data: payload,
      );
      print('response: ${res.data}');
      if (res.data == 'OK' && mounted) {
        Navigator.pop(context);
      }
    } catch (e) {
      print('Error: $e');
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFFF5F4FF),
      appBar: AppBar(
        backgroundColor: const Color(0xFF8676FF),
        title: const Text(
          "Enter Collection Details",
          style: TextStyle(color: Colors.white),
        ),
        leading: IconButton(
          icon: const Icon(Icons.arrow_back, color: Colors.white),
          onPressed: () {
            Navigator.pop(context);
          },
        ),
      ),
      body:
          onStart
              ? const Center(
                child: CircularProgressIndicator(color: Colors.blue),
              )
              : SingleChildScrollView(
                scrollDirection: Axis.vertical,
                padding: const EdgeInsets.all(16),
                child: Card(
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(32),
                  ),
                  elevation: 4,
                  child: Padding(
                    padding: const EdgeInsets.all(20.0),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      mainAxisAlignment: MainAxisAlignment.start,
                      children: [
                        Center(
                          child: Container(
                            padding: const EdgeInsets.symmetric(
                              horizontal: 24,
                              vertical: 8,
                            ),
                            decoration: BoxDecoration(
                              color: const Color(0xFFF88E8E),
                              borderRadius: BorderRadius.circular(20),
                            ),
                            child: const Text(
                              "NOT COLLECTED",
                              style: TextStyle(
                                color: Colors.white,
                                fontWeight: FontWeight.bold,
                              ),
                            ),
                          ),
                        ),
                        const SizedBox(height: 24),
                        Row(
                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          children: [
                            Icon(Icons.calendar_today_outlined, size: 20),
                            SizedBox(width: 5),
                            Text(
                              data['requestDate'].toString(),
                              style: TextStyle(fontWeight: FontWeight.bold),
                            ),
                            Spacer(),
                            Icon(Icons.production_quantity_limits),
                            SizedBox(width: 8),
                            Text(
                              data['qty'].toString(),
                              style: TextStyle(fontWeight: FontWeight.bold),
                            ),
                          ],
                        ),
                        const SizedBox(height: 16),
                        Text(
                          data['collectionName'],
                          style: TextStyle(
                            fontWeight: FontWeight.bold,
                            fontSize: 20,
                          ),
                        ),
                        const SizedBox(height: 16),
                        TextField(
                          controller: qtyController,
                          decoration: InputDecoration(
                            hintText: "Acutal Qty Collected",
                            border: OutlineInputBorder(),
                          ),
                          keyboardType: TextInputType.number,
                        ),
                        const SizedBox(height: 12),
                        GestureDetector(
                          onTap: () async {
                            final pickedDate = await showDatePicker(
                              context: context,
                              initialDate: selectedDate ?? DateTime.now(),
                              firstDate: DateTime(2000),
                              lastDate: DateTime(2100),
                            );
                            if (pickedDate != null) {
                              setState(() {
                                selectedDate = pickedDate;
                                dateController.text =
                                    "${pickedDate.year}-${pickedDate.month.toString().padLeft(2, '0')}-${pickedDate.day.toString().padLeft(2, '0')}";
                              });
                            }
                          },
                          child: AbsorbPointer(
                            child: TextField(
                              controller: dateController,
                              decoration: InputDecoration(
                                hintText: "Date",
                                border: OutlineInputBorder(),
                              ),
                            ),
                          ),
                        ),
                        const SizedBox(height: 12),
                        TextField(
                          controller: invoiceController,
                          decoration: InputDecoration(
                            hintText: "Invoice Number",
                            border: OutlineInputBorder(),
                          ),
                        ),
                        const SizedBox(height: 12),
                        TextField(
                          controller: remarkController,
                          decoration: InputDecoration(
                            hintText: "Remark",
                            border: OutlineInputBorder(),
                          ),
                        ),
                        const SizedBox(height: 24),
                        const Text(
                          "OTP WILL BE SENT TO",
                          style: TextStyle(
                            fontWeight: FontWeight.bold,
                            fontSize: 16,
                          ),
                        ),
                        const SizedBox(height: 12),
                        Row(
                          children: [
                            Expanded(
                              child: TextField(
                                controller: mobileNoController,
                                decoration: InputDecoration(
                                  hintText: "Mobile No.",
                                  border: OutlineInputBorder(),
                                ),
                                // keyboardType: TextInputType.t,
                              ),
                            ),
                            const SizedBox(width: 8),
                            ElevatedButton(
                              style: ElevatedButton.styleFrom(
                                backgroundColor: Colors.grey[700],
                                padding: const EdgeInsets.symmetric(
                                  horizontal: 16,
                                  vertical: 12,
                                ),
                              ),
                              onPressed:
                                  isLoading
                                      ? null
                                      : () {
                                        setState(() {
                                          mobileNoController.clear();
                                        });
                                      },
                              child: const Text(
                                "CHANGE",
                                style: TextStyle(
                                  color: Colors.white,
                                  fontWeight: FontWeight.bold,
                                ),
                              ),
                            ),
                          ],
                        ),

                        const SizedBox(height: 20),
                        ElevatedButton(
                          style: ElevatedButton.styleFrom(
                            backgroundColor: const Color(0xFF8676FF),
                            minimumSize: const Size.fromHeight(50),
                            shape: RoundedRectangleBorder(
                              borderRadius: BorderRadius.circular(16),
                            ),
                          ),
                          onPressed: () {
                            _requestOTP();
                          },
                          child: const Text(
                            "SEND OTP",
                            style: TextStyle(
                              color: Colors.white,
                              letterSpacing: 1.2,
                            ),
                          ),
                        ),
                        const SizedBox(height: 16),
                        otpSend && otpMsg == 'OTP Not Verified'
                            ? Row(
                              children: [
                                Expanded(
                                  child: TextField(
                                    controller: otpController,
                                    decoration: InputDecoration(
                                      hintText: "Enter OTP",
                                      border: OutlineInputBorder(),
                                    ),
                                    keyboardType: TextInputType.number,
                                  ),
                                ),
                                const SizedBox(width: 20),
                                ElevatedButton(
                                  style: ElevatedButton.styleFrom(
                                    backgroundColor: Colors.lightBlueAccent,
                                    padding: const EdgeInsets.symmetric(
                                      horizontal: 16,
                                      vertical: 12,
                                    ),
                                  ),
                                  onPressed: _verifyOTP,
                                  child: const Text(
                                    "VERIFY",
                                    style: TextStyle(
                                      color: Colors.white,
                                      fontWeight: FontWeight.bold,
                                    ),
                                  ),
                                ),
                              ],
                            )
                            : Text(
                              otpMsg,
                              style: TextStyle(
                                fontSize: 20,
                                fontWeight: FontWeight.bold,
                              ),
                            ),
                        const SizedBox(height: 24),
                        Row(
                          children: [
                            Center(
                              child: Text(
                                photoTxt,
                                style: TextStyle(
                                  fontWeight: FontWeight.bold,
                                  fontSize: 16,
                                  color: Colors.grey,
                                ),
                              ),
                            ),
                            const SizedBox(width: 20),
                            ElevatedButton(
                              onPressed: _takePhoto,
                              child: const Text(
                                'Take Photo',
                                style: TextStyle(
                                  fontSize: 15,
                                  fontWeight: FontWeight.bold,
                                ),
                              ),
                            ),
                          ],
                        ),

                        // Flexible(
                        //   child: GridView.builder(
                        //     padding: EdgeInsets.all(8),
                        //     gridDelegate:
                        //         SliverGridDelegateWithFixedCrossAxisCount(
                        //           crossAxisCount: 3,
                        //           crossAxisSpacing: 8,
                        //           mainAxisSpacing: 8,
                        //         ),
                        //     itemCount: _capturedImages.length,
                        //     itemBuilder: (context, index) {
                        //       return Image.file(
                        //         File(_capturedImages[index].path),
                        //         fit: BoxFit.cover,
                        //       );
                        //     },
                        //   ),
                        // ),

                        // if (_imagesList.isNotEmpty)
                        //   Container(
                        //     padding: EdgeInsets.all(8),
                        //     child: ListView.builder(
                        //       scrollDirection: Axis.vertical,
                        //       shrinkWrap: true,
                        //       itemCount: _imagesList.length,
                        //       itemBuilder: (context, index) {
                        //         return Padding(
                        //           padding: const EdgeInsets.all(8.0),
                        //           child: Image.memory(_imagesList[index]),
                        //         );
                        //       },
                        //     ),
                        //   ),
                        // // Container(
                        // //   height: 100,
                        // //   child: GridView.builder(
                        // //     gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
                        // //       crossAxisCount: 2,
                        // //     ),
                        // //     itemBuilder: (context, index) {
                        // //       return Image.memory(_imagesList[index]);
                        // //     },
                        // //   ),
                        // // ),
                      ],
                    ),
                  ),
                ),
              ),
      bottomNavigationBar: SafeArea(
        child: Container(
          margin: const EdgeInsets.all(16),
          height: 50,
          width: double.infinity,
          child: ElevatedButton(
            style: ElevatedButton.styleFrom(
              backgroundColor: Colors.blue,
              shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(16),
              ),
            ),
            onPressed: _submit,
            child: const Text(
              "SUBMIT",
              style: TextStyle(
                fontWeight: FontWeight.bold,
                color: Colors.white,
                fontSize: 16,
              ),
            ),
          ),
        ),
      ),
    );
  }

  // final ImagePicker _picker = ImagePicker();
  // Uint8List? _images;
  // final List<Uint8List> _imagesList = [];

  // Future<void> _captureImage() async {
  //   final XFile? image = await _picker.pickImage(source: ImageSource.camera);

  //   if (image != null) {
  //     var data = await image.readAsBytes();
  //     setState(() {
  //       // _images = data;
  //       if (kDebugMode) {
  //         _imagesList.add(data);
  //         print("Length: ${_imagesList.length}");
  //       }
  //     });
  //   }
  // }
}
