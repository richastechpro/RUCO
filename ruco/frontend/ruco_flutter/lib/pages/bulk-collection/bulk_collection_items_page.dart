import 'package:flutter/material.dart';
import 'package:ruco_flutter/client/dio-client.dart';
import 'package:ruco_flutter/constant/environment.dart';

import '../../constant/routes.dart';

class BulkCollectionItemsPage extends StatefulWidget {
  const BulkCollectionItemsPage({super.key});

  @override
  State<BulkCollectionItemsPage> createState() =>
      _BulkCollectionItemsPageState();
}

class _BulkCollectionItemsPageState extends State<BulkCollectionItemsPage> {
  bool isLoading = false;
  bool onStart = true;
  List<dynamic> collections = [];
  late final int id;
  bool _isInitialized = false;
  String collectionName = "";

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
      fetchData();
    }
  }

  Future<void> fetchData() async {
    setState(() => isLoading = true);

    try {
      final String url = prod ? prodUrl : devUrl;
      final res = await DioClient.dio.get('$url/collection/bulk/$id');
      final dynamic fetchedData = res.data;
      print(fetchedData);

      setState(() {
        isLoading = false;
        onStart = false;
        collectionName = fetchedData['collectionName'];
        collections.addAll(fetchedData['items']);
        _isInitialized = true;
      });
    } catch (e) {
      print('Error fetching data: $e');
      setState(() {
        isLoading = false;
        onStart = false;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: const Color.fromARGB(255, 115, 101, 255),
        title: Text(collectionName, style: TextStyle(color: Colors.white)),
        leading: IconButton(
          icon: const Icon(Icons.arrow_back, color: Colors.white),
          onPressed: () {
            Navigator.pop(context);
          },
        ),
      ),
      body: Container(
        decoration: const BoxDecoration(
          image: DecorationImage(
            image: AssetImage('assets/background_new.png'),
            fit: BoxFit.cover,
          ),
        ),
        child:
            isLoading && onStart && collections.isNotEmpty
                ? const Center(
                  child: CircularProgressIndicator(color: Colors.blueAccent),
                )
                : ListView.builder(
                  padding: const EdgeInsets.all(16),
                  itemCount: collections.length,
                  itemBuilder: (context, index) {
                    if (index < collections.length) {
                      final item = collections[index];
                      final isCollected =
                          item['collectedStatus'] == 'Collected';
                      return Card(
                        shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(28),
                        ),
                        margin: const EdgeInsets.only(bottom: 24),
                        child: Padding(
                          padding: const EdgeInsets.all(20),
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Align(
                                alignment: Alignment.center,
                                child: Container(
                                  padding: const EdgeInsets.symmetric(
                                    horizontal: 16,
                                    vertical: 4,
                                  ),
                                  decoration: BoxDecoration(
                                    color:
                                        isCollected
                                            ? const Color(0xFFC7E76E)
                                            : const Color(0xFFF9B5B5),
                                    borderRadius: BorderRadius.circular(20),
                                  ),
                                  child: Text(
                                    item['collectedStatus'],
                                    style: const TextStyle(
                                      fontWeight: FontWeight.bold,
                                    ),
                                  ),
                                ),
                              ),
                              const SizedBox(height: 16),
                              Text(
                                item['fboBranchName'],
                                style: const TextStyle(
                                  fontWeight: FontWeight.bold,
                                  fontSize: 18,
                                ),
                              ),
                              const SizedBox(height: 8),
                              Row(
                                children: [
                                  const Icon(Icons.calendar_month),
                                  const SizedBox(width: 6),
                                  Text(item['date']),
                                  const Spacer(),
                                  const Icon(Icons.inventory_2_outlined),
                                  const SizedBox(width: 6),
                                  Text(item['qty'].toString()),
                                ],
                              ),
                              const SizedBox(height: 16),
                              ElevatedButton(
                                style: ElevatedButton.styleFrom(
                                  backgroundColor:
                                      isCollected
                                          ? const Color.fromARGB(255, 0, 85, 36)
                                          : const Color.fromARGB(
                                            255,
                                            115,
                                            101,
                                            255,
                                          ),
                                  shape: RoundedRectangleBorder(
                                    borderRadius: BorderRadius.circular(32),
                                  ),
                                  minimumSize: const Size.fromHeight(48),
                                ),
                                onPressed: () async {
                                  if (isCollected) return;
                                  if (mounted) {
                                    dynamic res = await Navigator.pushNamed(
                                      context,
                                      Routes.bulkCollectionCollect,
                                      arguments: {
                                        'id': item['id'],
                                        'collectionName': item['fboBranchName'],
                                        'qty': item['qty'],
                                        'date': item['date'],
                                      },
                                    );
                                    if (res == 'OK' && mounted) {
                                      setState(() {
                                        collections.clear();
                                        _isInitialized = false;
                                      });
                                      await fetchData();
                                    }
                                  }
                                },
                                child:
                                    isCollected
                                        ? const Text(
                                          'Collected',
                                          style: TextStyle(
                                            fontWeight: FontWeight.bold,
                                            letterSpacing: 1.2,
                                            color: Colors.white,
                                          ),
                                        )
                                        : const Text(
                                          'Collect',
                                          style: TextStyle(
                                            fontWeight: FontWeight.bold,
                                            letterSpacing: 1.2,
                                            color: Colors.white,
                                          ),
                                        ),
                              ),
                            ],
                          ),
                        ),
                      );
                    } else {
                      return const Padding(
                        padding: EdgeInsets.symmetric(vertical: 16),
                        child: Center(
                          child: CircularProgressIndicator(color: Colors.blue),
                        ),
                      );
                    }
                  },
                ),
      ),
    );
  }
}
