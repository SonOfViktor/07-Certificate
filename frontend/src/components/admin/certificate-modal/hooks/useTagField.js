import {useEffect} from 'react';
import {useForm} from 'react-hook-form';

export const useTagField = isSubmitting => {
  const {
    watch,
    control: tagControl,
    reset: resetTag,
    trigger: tagTrigger,
  } = useForm({
    mode: 'onTouched',
    defaultValues: {
      tagField: '',
    },
  });

  const tag = watch('tagField');

  useEffect(() => {
    if (isSubmitting) {
      resetTag();
    }
  }, [isSubmitting, resetTag]);

  return {
    tag,
    tagControl,
    tagTrigger,
    resetTag,
  };
};
