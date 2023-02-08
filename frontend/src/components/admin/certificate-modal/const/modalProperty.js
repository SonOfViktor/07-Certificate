const NAME_PATTERN = /^[a-zA-ZА-ЯЁа-яё][a-zA-ZА-ЯЁа-яё\d\s_]{5,29}$/;
const DESCRIPTION_PATTERN =
  /^[a-zA-ZА-ЯЁа-яё][a-zA-ZА-ЯЁа-яё\d\s,;.:?!"'%()-]{11,999}$/;
const TAG_PATTERN = /^[a-zA-Z\d]{3,15}$|^[А-ЯЁа-яё\d]{3,15}$/;

export const backgroundProperty = {
  backdrop: {style: {backgroundColor: 'rgba(0, 0, 0, 0.3)'}},
};

const generalRules = {
  required: 'The field must not be empty',
};

export const createRules = certificate => ({
  name: {
    ...generalRules,
    pattern: {
      value: NAME_PATTERN,
      message: 'Title must contain 6 - 30 Latin or Cyrillic letters and digits',
    },
  },
  duration: {
    ...generalRules,
    min: {
      value: 1,
      message: 'Duration must not be less than 1',
    },
    max: {
      value: 99,
      message: 'Duration must be less than 100',
    },
  },
  category: {
    ...generalRules,
  },
  description: {
    ...generalRules,
    pattern: {
      value: DESCRIPTION_PATTERN,
      message: 'Description must contain 12 - 1000 letters and digits',
    },
  },
  price: {
    ...generalRules,
    min: {
      value: 0.01,
      message: 'Price must be more than 0',
    },
    max: {
      value: 999.99,
      message: 'Price must be less than 1000',
    },
  },
  image: {
    required: {
      value: !certificate,
      message: 'The field must not be empty',
    },
    validate: {
      maxSize: value => {
        if (value && value[0].size > 3 * 1024 * 1024) {
          return 'Max file size must be less 3 Mb';
        }
      },
      imageType: value => {
        if (value && !/image\/(jpeg|jpg|png)/.test(value[0].type)) {
          return 'File must be an image with jpeg, jpg or png extension';
        }
      },
    },
  },
});

export const tagRules = {
  validate: {
    allPattern: value => {
      const isValid = value.every(tag => TAG_PATTERN.test(tag.name));
      if (!isValid) {
        return 'Tag name must contain 3 - 15 Latin or Cyrillic letters and digits';
      }
    },
    maxLength: value => {
      if (value.length > 20) {
        return 'Max quantity of tags is 20';
      }
    },
    duplicates: value => {
      const names = value.map(v => v.name);
      const isDuplicate = names.some(
        (item, index) => index !== names.indexOf(item)
      );
      if (isDuplicate) {
        return "The tag list can't contain duplicates";
      }
    },
  },
};
