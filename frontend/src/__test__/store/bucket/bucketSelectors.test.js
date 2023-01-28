import {
  selectBucket,
  selectBucketLength,
  selectBucketTotalPrice,
} from '../../../store/bucket/bucketSelectors';
import {bucketData} from './testData';

const state = {bucket: bucketData};
const emptyState = {bucket: []};

describe('bucket selectors', () => {
  test('select bucket from state', () => {
    expect(selectBucket(state)).toEqual(bucketData);
  });

  test('select bucket total price', () => {
    expect(selectBucketTotalPrice(state)).toBe('936.72');
  });

  test('select initial bucket total price', () => {
    expect(selectBucketTotalPrice(emptyState)).toBe('0.00');
  });

  test('select total item amount in the bucket', () => {
    expect(selectBucketLength(state)).toBe(6);
  });

  test('select total item amount in the empty bucket', () => {
    expect(selectBucketLength(emptyState)).toBe(0);
  });
});
