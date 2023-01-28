export const createCertificate = (
  id,
  name,
  description,
  duration,
  price,
  tags
) => ({
  giftCertificateId: id,
  name,
  description,
  duration,
  price,
  tags,
});

export const createCertificateList = quantity => {
  return new Array(quantity)
    .fill(null)
    .map((_, index) =>
      createCertificate(
        index + 1,
        `Certificate${index + 1}`,
        'description',
        30,
        50,
        [{name: 'tag1'}, {name: 'tag2'}, {name: 'tag3'}]
      )
    );
};
