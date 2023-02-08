import {nanoid} from '@reduxjs/toolkit';
import {useController} from 'react-hook-form';
import {tagRules} from '../const/modalProperty';

export const useTags = (control, tagTrigger, tag, resetTag) => {
  const {
    field,
    formState: {errors},
  } = useController({
    control,
    rules: tagRules,
    name: 'tags',
  });

  const handleTags = async () => {
    if (await tagTrigger('tagField', {shouldFocus: true})) {
      field.onChange(field.value.concat({id: nanoid(), name: tag}));
      resetTag();
    }
  };

  const handleDelete = item => {
    field.onChange(field.value.filter(tagElement => tagElement.id !== item));
  };

  return {
    field,
    tagsErrorMessage: errors.tags?.message,
    handleTags,
    handleDelete,
  };
};
