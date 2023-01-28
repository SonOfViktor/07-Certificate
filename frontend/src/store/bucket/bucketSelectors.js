export const selectBucket = state => state.bucket;

export const selectBucketTotalPrice = state =>
  state.bucket
    .reduce(
      (sum, certificate) => {
        sum += certificate.price * certificate.amount;
        return sum
      },
      0
    )
    .toFixed(2);

export const selectBucketLength = state =>
  state.bucket.reduce(
    (quantity, certificate) => {
      quantity += certificate.amount;
      return quantity;
    },
    0
  );
