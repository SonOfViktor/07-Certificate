import {
  addBucketCertificate,
  bucketReducer,
  deleteBucketCertificate,
  resetBucket,
} from '../../../store/bucket/bucketSlice';
import {bucketData} from './testData';

describe('add bucket action', () => {
  const certificate = {
    giftCertificateId: 1,
    name: 'Certificate1',
    price: 500,
  };

  test('add with initial state', () => {
    const expected = [
      {giftCertificateId: 1, name: 'Certificate1', price: 500, amount: 1},
    ];
    const actual = bucketReducer(undefined, addBucketCertificate(certificate));

    expect(actual).toEqual(expected);
  });

  test('add new certificate', () => {
    const newCertificate = {
      giftCertificateId: 4,
      name: 'Certificate4',
      price: 40,
    };

    const actual = bucketReducer(
      bucketData,
      addBucketCertificate(newCertificate)
    );
    newCertificate.amount = 1;
    const expected = [...bucketData, newCertificate];

    expect(actual).toEqual(expected);
  });

  test('add existing certificate', () => {
    const actual = bucketReducer(bucketData, addBucketCertificate(certificate));
    const expected = bucketData.map(cert => ({...cert}));
    expected[0].amount++;

    expect(actual).toEqual(expected);
  });

  describe('delete bucket action', () => {
    test('delete certificate from the bucket', () => {
      const actual = bucketReducer([...bucketData], deleteBucketCertificate(1));
      /* eslint-disable no-unused-vars */
      const [_, ...expected] = bucketData;

      expect(actual).toEqual(expected);
    });

    test('reduce certificate amount in the bucket', () => {
      const actual = bucketReducer([...bucketData], deleteBucketCertificate(2));
      const expected = bucketData.map(cert => ({...cert}));
      expected[1].amount--;

      expect(actual).toEqual(expected);
    });
  });

  describe('reset bucket action', () => {
    test('reset bucket', () => {
      const actual = bucketReducer([...bucketData], resetBucket());
      const expected = [];

      expect(actual).toEqual(expected);
    });
  });
});
